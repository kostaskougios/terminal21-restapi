package org.terminal21.serverapp.bundled

import org.terminal21.client.components.*
import org.terminal21.client.components.frontend.ThemeToggle
import org.terminal21.client.*
import org.terminal21.server.Dependencies
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class SettingsApp extends ServerSideApp:
  override def name = "Settings"

  override def description = "Terminal21 Settings"

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions
      .withNewSession("frontend-settings", "Settings")
      .connect: session =>
        given ConnectedSession = session
        new SettingsPage().run()

class SettingsPage(using session: ConnectedSession):
  import Model.unitModel
  val themeToggle = ThemeToggle()
  def run()       =
    controller.eventsIterator.lastOption

  def components = Seq(themeToggle)

  def controller = Controller(components)
