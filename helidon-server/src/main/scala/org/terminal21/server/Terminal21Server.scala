package org.terminal21.server

import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.websocket.WsRouting
import org.terminal21.server.ui.UiWebSocket

@main def terminal21Server(): Unit =
  val routesBuilder = HttpRouting.builder()
  WsRouting
    .builder()
    .endpoint("/ui/session", new UiWebSocket)
    .build
  LogConfig.configureRuntime()
  val server        = WebServer.builder.port(8080).routing(routesBuilder).build.start
  try
    println("Terminal 21 Server started and listening on http://localhost:8080")
    while true do Thread.sleep(86400)
  finally server.stop()
