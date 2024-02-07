package org.terminal21.serverapp.bundled

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.Session
import org.terminal21.server.Dependencies
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class ServerStatusApp extends ServerSideApp:
  override def name: String        = "Server Status"
  override def description: String = "Status of the server."

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions.withNewSession("server-status", "Server Status"): session =>
      given ConnectedSession = session
      val sessionService     = dependencies.sessionsService
      val sessions           = sessionService.allSessions
      val sessionsTable      = QuickTable(
        caption = Some("All sessions"),
        rows = sessions.map: session =>
          Seq(Text(text = session.id), Text(text = session.name), if session.isOpen then CheckIcon() else NotAllowedIcon(), actionsFor(session, sessionService))
      )
        .headers("Id", "Name", "Is Open", "Actions")

      Seq(sessionsTable).render()
      session.waitTillUserClosesSession()

  private def actionsFor(session: Session, sessionsService: ServerSessionsService)(using ConnectedSession): UiElement =
    if session.isOpen then
      Button(text = "Close", size = Some("sm"))
        .withLeftIcon(CloseIcon())
        .onClick: () =>
          sessionsService.terminateAndRemove(session)
    else NotAllowedIcon()
