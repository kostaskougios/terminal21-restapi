package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.slf4j.LoggerFactory
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.server.Terminal21Server.getClass
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class AppManager(serverSideSessions: ServerSideSessions, fiberExecutor: FiberExecutor, apps: Seq[ServerSideApp]):
  private val logger = LoggerFactory.getLogger(getClass)

  def start(): Unit =
    fiberExecutor.submit:
      logger.info("Starting AppManager")
      serverSideSessions.withNewSession("app-manager", "Apps"): session =>
        given ConnectedSession = session

        val appLinks = apps.map: app =>
          Link(text = app.name).onClick: () =>
            startApp(app)

        (Seq(
          Paragraph(text = "Here you can run the apps installed on the server")
        ) ++ appLinks).render()

        session.waitTillUserClosesSession()

  private def startApp(app: ServerSideApp): Unit =
    app.createSession(serverSideSessions)

trait AppManagerBeans:
  def serverSideSessions: ServerSideSessions
  def fiberExecutor: FiberExecutor
  def apps: Seq[ServerSideApp]
  lazy val appManager = new AppManager(serverSideSessions, fiberExecutor, apps)
