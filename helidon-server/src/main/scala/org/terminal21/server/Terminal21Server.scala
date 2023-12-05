package org.terminal21.server

import functions.fibers.FiberExecutor
import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting
import org.terminal21.config.Config

@main def terminal21Server(): Unit =
  val config        = Config.Default
  val dependencies  = new Dependencies
  val routesBuilder = HttpRouting.builder()
  Routes.register(dependencies, routesBuilder)
  Routes.static(routesBuilder)

  LogConfig.configureRuntime()
  val server = WebServer.builder
    .port(config.port)
    .routing(routesBuilder)
    .addRouting(Routes.ws(dependencies))
    .build
    .start
  try
    println(s"Terminal 21 Server started and listening on http://localhost:${config.port}")
    while true do
      Thread.sleep(86400 * 1000)
      println("One more day passed...")
  finally
    println("Server stopping....")
    server.stop()
