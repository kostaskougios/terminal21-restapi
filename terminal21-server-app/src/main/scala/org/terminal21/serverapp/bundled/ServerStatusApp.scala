package org.terminal21.serverapp.bundled

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.server.Dependencies
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class ServerStatusApp extends ServerSideApp:
  override def name: String        = "Server Status"
  override def description: String = "Status of the server."

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions.withNewSession("server-status", "Server Status"): session =>
      given ConnectedSession = session
      val sessionService     = dependencies.sessionsService
      val sessions           = sessionService.allSessions
      val sessionsTable      = QuickTable(caption = Some("All sessions"))
        .headers("Id", "Name", "Is Open")
        .rows(
          sessions.map: session =>
            Seq(session.id, session.name, session.isOpen)
        )

      Seq(sessionsTable).render()
      session.leaveSessionOpenAfterExiting()
