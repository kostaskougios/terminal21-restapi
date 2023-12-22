package org.terminal21.server.service

import org.slf4j.LoggerFactory
import org.terminal21.model.*
import org.terminal21.server.json.UiEvent
import org.terminal21.server.model.SessionState
import org.terminal21.server.utils.{ListenerFunction, NotificationRegistry}
import org.terminal21.ui.std.SessionsService

import java.util.UUID

class ServerSessionsService extends SessionsService:
  private val logger = LoggerFactory.getLogger(getClass)

  private val sessions = collection.concurrent.TrieMap.empty[Session, SessionState]

  private val sessionChangeNotificationRegistry      = new NotificationRegistry[Seq[Session]]
  private val sessionStateChangeNotificationRegistry = new NotificationRegistry[(Session, SessionState)]

  def sessionById(sessionId: String): Session =
    sessions.keys.find(_.id == sessionId).getOrElse(throw new IllegalArgumentException(s"Invalid session id = $sessionId"))

  def notifyMeWhenSessionsChange(listener: ListenerFunction[Seq[Session]]): Unit =
    sessionChangeNotificationRegistry.addAndNotify(allSessions)(listener)

  def removeSession(session: Session): Unit =
    sessions -= session
    sessionChangeNotificationRegistry.notifyAll(allSessions)

  override def terminateSession(session: Session): Unit =
    val state = sessions.getOrElse(session, throw new IllegalArgumentException(s"Session ${session.id} doesn't exist"))
    state.eventsNotificationRegistry.notifyAll(SessionClosed("-"))
    sessions -= session
    sessions += session.close -> state.close
    sessionChangeNotificationRegistry.notifyAll(allSessions)

  override def createSession(id: String, name: String): Session =
    val s     = Session(id, name, UUID.randomUUID().toString, true)
    logger.info(s"Creating session $s")
    sessions.keys.toList.foreach(s => if s.id == id then sessions.remove(s))
    val state = SessionState("""{ "elements" : [] }""", new NotificationRegistry)
    sessions += s -> state
    sessionChangeNotificationRegistry.notifyAll(allSessions)
    s

  def allSessions: Seq[Session] = sessions.keySet.toList

  def notifyMeWhenSessionChanges(f: ListenerFunction[(Session, SessionState)]): Unit =
    sessionStateChangeNotificationRegistry.add(f)
    for (session, state) <- sessions do f(session, state)

  override def setSessionJsonState(session: Session, newStateJson: String): Unit =
    val oldV = sessions(session)
    val newV = oldV.withNewState(newStateJson)
    sessions += session -> newV
    sessionStateChangeNotificationRegistry.notifyAll((session, newV))
    logger.info(s"Session $session new state $newStateJson")

  def addEvent(event: UiEvent): Unit =
    val e = event match
      case org.terminal21.server.json.OnClick(_, key)         => OnClick(key)
      case org.terminal21.server.json.OnChange(_, key, value) => OnChange(key, value)

    val session = sessionById(event.sessionId)
    val state   = sessions(session)
    state.eventsNotificationRegistry.notifyAll(e)

  def notifyMeOnSessionEvents(session: Session)(listener: ListenerFunction[CommandEvent]): Unit =
    val state = sessions(session)
    state.eventsNotificationRegistry.add(listener)

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
