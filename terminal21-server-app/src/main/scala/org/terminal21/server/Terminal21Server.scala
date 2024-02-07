package org.terminal21.server

import functions.fibers.FiberExecutor
import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting
import org.slf4j.LoggerFactory
import org.terminal21.config.Config

import java.net.InetAddress

object Terminal21Server:
  private val logger                        = LoggerFactory.getLogger(getClass)
  def start(port: Option[Int] = None): Unit =
    FiberExecutor.withFiberExecutor: executor =>
      val portV         = port.getOrElse(Config.Default.port)
      val dependencies  = new Dependencies(executor)
      val routesBuilder = HttpRouting.builder()
      Routes.register(dependencies, routesBuilder)
      Routes.static(routesBuilder)

      LogConfig.configureRuntime()
      val server = WebServer.builder
        .port(portV)
        .routing(routesBuilder)
        .addRouting(Routes.ws(dependencies))
        .build
        .start

      dependencies.appManager.start()
      if !server.isRunning then throw new IllegalStateException("Server failed to start")
      try
        logger.info(s"Terminal 21 Server started. Please open http://localhost:$portV/ui for the user interface")
        val hostname = InetAddress.getLocalHost.getHostName
        logger.info(s"""
             |Files under ~/.terminal21/web will be served under /web
             |Clients should set env variables:
             |TERMINAL21_HOST = $hostname
             |TERMINAL21_PORT = $portV
             |
             |if unset, they will point to localhost:8080
             |""".stripMargin)
        while true do
          Thread.sleep(86400 * 1000)
          logger.info("One more day passed...")
      finally
        logger.info("Server stopping....")
        server.stop()

@main def terminal21ServerMain(): Unit = Terminal21Server.start()
