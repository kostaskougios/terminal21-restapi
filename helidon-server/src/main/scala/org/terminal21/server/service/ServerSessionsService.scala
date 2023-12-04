package org.terminal21.server.service

import org.slf4j.LoggerFactory
import org.terminal21.server.model.SessionState
import org.terminal21.ui.std.SessionsService
import org.terminal21.ui.std.model.Session

import java.util.UUID

class ServerSessionsService extends SessionsService:
  private val logger   = LoggerFactory.getLogger(getClass)
  private val sessions = collection.concurrent.TrieMap.empty[Session, SessionState]

  override def terminateSession(session: Session): Unit =
    logger.info(s"Terminating session $session")

  override def createSession(id: String, name: String): Session =
    val s = Session(id, name, UUID.randomUUID().toString)
    logger.info(s"Creating session $s")
    sessions.keys.toList.foreach(s => if s.id == id then sessions.remove(s))
    sessions += s -> SessionState(s)
    notifySessionChanged()
    s

  def allSessions: Seq[Session] = sessions.keySet.toList

  private val waitObj                      = new Object
  private def notifySessionChanged(): Unit = waitObj.synchronized { waitObj.notifyAll() }

  def waitForSessionsChange(): Unit =
    waitObj.synchronized { waitObj.wait() }

  def sessionState(session: Session): SessionState  = sessions.getOrElse(session, throw new IllegalArgumentException(s"can't find session $session"))
  def sessionState(sessionId: String): SessionState =
    val s = sessions.keys.find(_.id == sessionId).getOrElse(throw new IllegalArgumentException(s"can't find session id $sessionId"))
    sessionState(s)

  def modifySessionState(session: Session)(f: SessionState => SessionState): Unit =
    val oldState = sessionState(session)
    val newState = f(oldState)
    sessions += session -> newState
    oldState.notifyChanged()
    logger.info(s"Session $session new state $newState")

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
