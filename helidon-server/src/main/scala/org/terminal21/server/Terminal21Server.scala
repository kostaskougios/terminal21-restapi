package org.terminal21.server

import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.staticcontent.StaticContentService
import io.helidon.webserver.websocket.WsRouting
import org.terminal21.server.ui.UiWebSocket

import java.nio.file.Path

@main def terminal21Server(): Unit =
  val routesBuilder = HttpRouting.builder()
  val staticContent = StaticContentService
    .builder(Path.of("../../terminal21-ui/build"))
    .welcomeFileName("index.html")
    .build
  routesBuilder.register("/ui", staticContent)
  val wsRouting     = WsRouting
    .builder()
    .endpoint("/ui/session", new UiWebSocket)
  LogConfig.configureRuntime()
  val server        = WebServer.builder.port(8080).routing(routesBuilder).addRouting(wsRouting).build.start
  try
    println("Terminal 21 Server started and listening on http://localhost:8080")
    while true do
      Thread.sleep(86400)
      println("One more day passed...")
  finally server.stop()
