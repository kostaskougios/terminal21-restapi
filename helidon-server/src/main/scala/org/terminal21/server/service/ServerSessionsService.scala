package org.terminal21.server.service

import org.slf4j.LoggerFactory
import org.terminal21.model.Session
import org.terminal21.server.model.SessionState
import org.terminal21.server.utils.{ListenerFunction, NotificationRegistry}
import org.terminal21.ui.std.SessionsService

import java.util.UUID

class ServerSessionsService extends SessionsService:
  private val logger = LoggerFactory.getLogger(getClass)

  private case class MapValue(notificationRegistry: NotificationRegistry[SessionState], sessionState: SessionState)
  private val sessions = collection.concurrent.TrieMap.empty[Session, MapValue]

  private val sessionChangeNotificationRegistry = new NotificationRegistry[Seq[Session]]

  def notifyMeWhenSessionsChange(listener: ListenerFunction[Seq[Session]]): Unit =
    sessionChangeNotificationRegistry.addAndNotify(allSessions)(listener)

  override def terminateSession(session: Session): Unit =
    logger.info(s"Terminating session $session")

  override def createSession(id: String, name: String): Session =
    val s = Session(id, name, UUID.randomUUID().toString)
    logger.info(s"Creating session $s")
    sessions.keys.toList.foreach(s => if s.id == id then sessions.remove(s))
    sessions += s -> MapValue(new NotificationRegistry, SessionState("""{ "elements" : [] }"""))
    sessionChangeNotificationRegistry.notifyAll(allSessions)
    s

  def allSessions: Seq[Session] = sessions.keySet.toList

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

  override def setSessionJsonState(session: Session, newStateJson: String): Unit =
    val oldV     = mapValueOf(session)
    val newState = SessionState(newStateJson)
    val newV     = oldV.copy(sessionState = newState)
    sessions += session -> newV
    val listenerCount = newV.notificationRegistry.notifyAll(newState)
    logger.info(s"Session $session new state $newStateJson , listeners: $listenerCount")

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
