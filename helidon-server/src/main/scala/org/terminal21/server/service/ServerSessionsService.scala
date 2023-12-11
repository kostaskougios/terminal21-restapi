package org.terminal21.server.service

import org.slf4j.LoggerFactory
import org.terminal21.model.Session
import org.terminal21.server.model.SessionState
import org.terminal21.server.utils.{ListenerFunction, NotificationRegistry}
import org.terminal21.ui.std.SessionsService

import java.util.UUID

class ServerSessionsService extends SessionsService:
  private val logger = LoggerFactory.getLogger(getClass)

  private val sessions = collection.concurrent.TrieMap.empty[Session, SessionState]

  private val sessionChangeNotificationRegistry      = new NotificationRegistry[Seq[Session]]
  private val sessionStateChangeNotificationRegistry = new NotificationRegistry[(Session, SessionState)]

  def notifyMeWhenSessionsChange(listener: ListenerFunction[Seq[Session]]): Unit =
    sessionChangeNotificationRegistry.addAndNotify(allSessions)(listener)

  override def terminateSession(session: Session): Unit =
    logger.info(s"Terminating session $session")

  override def createSession(id: String, name: String): Session =
    val s = Session(id, name, UUID.randomUUID().toString)
    logger.info(s"Creating session $s")
    sessions.keys.toList.foreach(s => if s.id == id then sessions.remove(s))
    sessions += s -> SessionState("""{ "elements" : [] }""")
    sessionChangeNotificationRegistry.notifyAll(allSessions)
    s

  def allSessions: Seq[Session] = sessions.keySet.toList

  def notifyMeWhenSessionChanges(f: ListenerFunction[(Session, SessionState)]): Unit =
    sessionStateChangeNotificationRegistry.add(f)
    for (session, state) <- sessions do f(session, state)

  override def setSessionJsonState(session: Session, newStateJson: String): Unit =
    val newV = SessionState(newStateJson)
    sessions += session -> newV
    sessionStateChangeNotificationRegistry.notifyAll((session, newV))
    logger.info(s"Session $session new state $newStateJson")

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
