package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.slf4j.LoggerFactory
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.server.Dependencies
import org.terminal21.server.Terminal21Server.getClass
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class AppManager(serverSideSessions: ServerSideSessions, fiberExecutor: FiberExecutor, apps: Seq[ServerSideApp], dependencies: Dependencies):
  private val logger = LoggerFactory.getLogger(getClass)

  def start(): Unit =
    fiberExecutor.submit:
      logger.info("Starting AppManager")
      serverSideSessions.withNewSession("app-manager", "Apps"): session =>
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
