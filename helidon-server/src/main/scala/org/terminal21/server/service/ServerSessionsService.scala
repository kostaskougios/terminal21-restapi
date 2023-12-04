package org.terminal21.server.service

import org.terminal21.server.model.SessionState
import org.terminal21.ui.std.SessionsService
import org.terminal21.ui.std.model.Session

class ServerSessionsService extends SessionsService:
  private val sessions                            = collection.concurrent.TrieMap.empty[String, SessionState]
  override def createSession(id: String): Session =
    val s = Session(id)
    sessions += id -> SessionState(s)
    s
