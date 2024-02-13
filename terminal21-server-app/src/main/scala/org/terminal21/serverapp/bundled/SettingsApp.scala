package org.terminal21.serverapp.bundled

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.ui.ThemeToggle
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.{Link, Text}
import org.terminal21.client.components.std.Paragraph
import org.terminal21.server.Dependencies
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class SettingsApp extends ServerSideApp:
  override def name = "Settings"

  override def description = "Settings for terminal21"

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions
      .withNewSession("frontend-settings", "Settings")
      .connect: session =>
        given ConnectedSession = session
        new SettingsAppInstance().run()

class SettingsAppInstance(using session: ConnectedSession):
  def run() =
    Seq(
      ThemeToggle(),
      Paragraph().withChildren(
        Text(text = "Have a question? Please ask at "),
        Link(
          text = "terminal21's discussion board",
          href = "https://github.com/kostaskougios/terminal21-restapi/discussions",
          color = Some("teal.500"),
          isExternal = Some(true)
        )
      )
    ).render()
    session.waitTillUserClosesSession()
