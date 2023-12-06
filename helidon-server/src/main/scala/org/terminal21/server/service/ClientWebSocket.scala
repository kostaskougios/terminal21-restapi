package org.terminal21.server.service

import io.helidon.websocket.{WsListener, WsSession}

class ClientWebSocket extends WsListener:
  override def onMessage(session: WsSession, text: String, last: Boolean) =
    println(s"got $text , $last")

trait ClientWebSocketBeans:
  lazy val clientWebSocket = new ClientWebSocket
