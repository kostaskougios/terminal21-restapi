package org.terminal21.server

import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.staticcontent.StaticContentService
import io.helidon.webserver.websocket.WsRouting
import org.terminal21.ui.std.SessionsServiceReceiverFactory

object Routes:
  def register(dependencies: Dependencies, rb: HttpRouting.Builder): Unit =
    import dependencies.*
    SessionsServiceReceiverFactory.newJsonSessionsServiceHelidonRoutes(sessionsService).routes(rb)

  def static(rb: HttpRouting.Builder): Unit =
    val staticContent = StaticContentService
      .builder("web")
      .welcomeFileName("index.html")
      .build
    rb.register("/ui", staticContent)

  def ws(dependencies: Dependencies): WsRouting.Builder =
    val b = WsRouting.builder
    b.endpoint("/ui/sessions", dependencies.sessionsWebSocket)
      .endpoint("/api/command-ws", dependencies.commandWebSocket.commandWebSocketListener.listener)
    b
