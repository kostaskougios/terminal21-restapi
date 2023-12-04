package org.terminal21.server.service

import org.slf4j.LoggerFactory
import org.terminal21.server.model.SessionState
import org.terminal21.ui.std.SessionsService
import org.terminal21.ui.std.model.Session

class ServerSessionsService extends SessionsService:
  private val logger                                            = LoggerFactory.getLogger(getClass)
  private val sessions                                          = collection.concurrent.TrieMap.empty[String, SessionState]
  override def createSession(id: String, name: String): Session =
    val s = Session(id, name)
    logger.info(s"Creating session $s")
    sessions += id -> SessionState(s)
    s

trait ServerSessionsServiceBeans:
  val sessionsService: ServerSessionsService = new ServerSessionsService
