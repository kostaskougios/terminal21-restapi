package org.terminal21.server

import functions.fibers.FiberExecutor
import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting
import org.slf4j.LoggerFactory
import org.terminal21.config.Config

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
      try
        logger.info(s"Terminal 21 Server started and listening on http://localhost:$portV")
        while true do
          Thread.sleep(86400 * 1000)
          logger.info("One more day passed...")
      finally
        logger.info("Server stopping....")
        server.stop()

@main def terminal21ServerMain(): Unit = Terminal21Server.start()
