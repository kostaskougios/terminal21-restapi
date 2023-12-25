package org.terminal21.server

import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.staticcontent.StaticContentService
import io.helidon.webserver.websocket.WsRouting
import org.slf4j.LoggerFactory
import org.terminal21.server.utils.Environment
import org.terminal21.ui.std.SessionsServiceReceiverFactory

import java.io.File
import java.nio.file.Path

object Routes:
  private val logger                                                      = LoggerFactory.getLogger(getClass)
  def register(dependencies: Dependencies, rb: HttpRouting.Builder): Unit =
    import dependencies.*
    SessionsServiceReceiverFactory.newJsonSessionsServiceHelidonRoutes(sessionsService).routes(rb)

  def static(rb: HttpRouting.Builder): Unit =
    val staticContent = StaticContentService
      .builder("web")
      .welcomeFileName("index.html")
      .build
    val webFolder     = new File(Environment.UserHome, ".terminal21/web")
    if !webFolder.exists() then
      logger.info(s"Creating $webFolder where static files can be placed.")
      webFolder.mkdirs()

    val publicContent = StaticContentService
      .builder(Path.of(Environment.UserHome, ".terminal21", "web"))
      .welcomeFileName("index.html")
      .build

    rb.register("/ui", staticContent)
    rb.register("/web", publicContent)

  def ws(dependencies: Dependencies): WsRouting.Builder =
    val b = WsRouting.builder
    b.endpoint("/ui/sessions", dependencies.sessionsWebSocket)
      .endpoint("/api/command-ws", dependencies.commandWebSocket.commandWebSocketListener.listener)
    b
