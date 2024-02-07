package org.terminal21.serverapp.bundled

import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class ServerStatusApp extends ServerSideApp:
  override def name: String = "Server Status"

  override def createSession(serverSideSessions: ServerSideSessions): Unit =
    println(s"$name creating session")
    ???
