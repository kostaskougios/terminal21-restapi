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
    sessions += s -> SessionState(s)
    s

  def allSessions: Seq[Session] = sessions.keySet.toList

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
