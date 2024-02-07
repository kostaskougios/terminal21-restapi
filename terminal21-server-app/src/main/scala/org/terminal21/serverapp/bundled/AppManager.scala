package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.slf4j.LoggerFactory
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.server.Terminal21Server.getClass
import org.terminal21.serverapp.ServerSideSessions

class AppManager(serverSideSessions: ServerSideSessions, fiberExecutor: FiberExecutor):
  private val logger = LoggerFactory.getLogger(getClass)

  def start(): Unit =
    fiberExecutor.submit:
      logger.info("Starting AppManager")
      serverSideSessions.withNewSession("app-manager", "Apps"): session =>
        given ConnectedSession = session

        Seq(
          Paragraph(text = "Here you can run the apps installed on the server")
        ).render()

        session.waitTillUserClosesSession()

trait AppManagerBeans:
  def serverSideSessions: ServerSideSessions
  def fiberExecutor: FiberExecutor
  lazy val appManager = new AppManager(serverSideSessions, fiberExecutor)
