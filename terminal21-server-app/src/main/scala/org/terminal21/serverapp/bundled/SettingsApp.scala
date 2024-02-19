package org.terminal21.serverapp.bundled

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.{ExternalLinkIcon, Link}
import org.terminal21.client.components.std.{Paragraph, Span}
import org.terminal21.client.components.ui.ThemeToggle
import org.terminal21.model.SessionOptions
import org.terminal21.server.Dependencies
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class SettingsApp extends ServerSideApp:
  override def name = "Settings"

  override def description = "Terminal21 Settings"

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions
      .withNewSession("frontend-settings", "Settings")
      .andOptions(SessionOptions(closeTabWhenTerminated = true))
      .connect: session =>
        given ConnectedSession = session
        new SettingsAppInstance().run()

class SettingsAppInstance(using session: ConnectedSession):
  def run() =
    Seq(
      ThemeToggle()
    ).render()
    session.waitTillUserClosesSession()
