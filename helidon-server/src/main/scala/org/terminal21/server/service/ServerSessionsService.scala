package org.terminal21.server.service

import org.slf4j.LoggerFactory
import org.terminal21.server.model.SessionState
import org.terminal21.server.utils.{ListenerFunction, NotificationRegistry}
import org.terminal21.ui.std.SessionsService
import org.terminal21.ui.std.model.Session

import java.util.UUID

class ServerSessionsService extends SessionsService:
  private val logger = LoggerFactory.getLogger(getClass)

  private case class MapValue(notificationRegistry: NotificationRegistry[SessionState], sessionState: SessionState)

  private val sessions = collection.concurrent.TrieMap.empty[Session, MapValue]

  override def terminateSession(session: Session): Unit =
    logger.info(s"Terminating session $session")

  override def createSession(id: String, name: String): Session =
    val s = Session(id, name, UUID.randomUUID().toString)
    logger.info(s"Creating session $s")
    sessions.keys.toList.foreach(s => if s.id == id then sessions.remove(s))
    sessions += s -> MapValue(new NotificationRegistry, SessionState(s))
    notifySessionChanged()
    s

  def allSessions: Seq[Session] = sessions.keySet.toList

  private val waitObj                      = new Object
  private def notifySessionChanged(): Unit = waitObj.synchronized { waitObj.notifyAll() }

  def waitForSessionsChange(): Unit =
    waitObj.synchronized { waitObj.wait() }

  private def mapValueOf(session: Session): MapValue =
    sessions.getOrElse(session, throw new IllegalArgumentException(s"can't find session $session"))

  def sessionState(session: Session): SessionState =
    mapValueOf(session).sessionState

  private def sessionMapValue(sessionId: String): MapValue =
    val s = sessions.keys.find(_.id == sessionId).getOrElse(throw new IllegalArgumentException(s"can't find session id $sessionId"))
    mapValueOf(s)

  def notifyMeWhenSessionChanges(sessionId: String)(f: ListenerFunction[SessionState]): Unit =
    val mv = sessionMapValue(sessionId)
    mv.notificationRegistry.addAndNotify(mv.sessionState)(f)

  def modifySessionState(session: Session)(f: SessionState => SessionState): Unit =
    val oldV     = mapValueOf(session)
    val newState = f(oldV.sessionState)
    val newV     = oldV.copy(sessionState = newState)
    sessions += session -> newV
    val listenerCount = newV.notificationRegistry.notifyAll(newState)
    logger.info(s"Session $session new state $newState , listeners: $listenerCount")

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
