package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.server.Dependencies
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class AppManager(serverSideSessions: ServerSideSessions, fiberExecutor: FiberExecutor, apps: Seq[ServerSideApp], dependencies: Dependencies):
  def start(): Unit =
    fiberExecutor.submit:
      serverSideSessions
        .withNewSession("app-manager", "Apps")
        .connect: session =>
          given ConnectedSession = session

          val appRows   = apps.map: app =>
            val link = Link(text = app.name).onClick: () =>
              startApp(app)
            Seq[UiElement](link, Text(text = app.description))
          val appsTable = QuickTable(
            caption = Some("Apps installed on the server"),
            rows = appRows
          ).headers("App Name", "Description")

          Seq(
            appsTable
          ).render()

          session.waitTillUserClosesSession()

  private def startApp(app: ServerSideApp): Unit =
    app.createSession(serverSideSessions, dependencies)

trait AppManagerBeans:
  def serverSideSessions: ServerSideSessions
  def fiberExecutor: FiberExecutor
  def apps: Seq[ServerSideApp]
  def dependencies: Dependencies
  lazy val appManager = new AppManager(serverSideSessions, fiberExecutor, apps, dependencies)
