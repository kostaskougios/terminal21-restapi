package org.terminal21.server.service

import io.helidon.websocket.{WsListener, WsSession}

class ClientWebSocket(serverSessionsService: ServerSessionsService) extends WsListener:
  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    text.split(":") match
      case Array("subscribe", sessionId) =>
//        serverSessionsService.subscribeToSessionEvents(sessionId)

trait ClientWebSocketBeans:
  def sessionsService: ServerSessionsService
  lazy val clientWebSocket = new ClientWebSocket(sessionsService)
