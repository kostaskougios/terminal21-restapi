package org.terminal21.server.ui

import io.helidon.websocket.{WsListener, WsSession}

// websocket: https://helidon.io/docs/v4/#/se/websocket
class UiWebSocket extends WsListener:
  override def onMessage(session: WsSession, text: String, last: Boolean) =
    println(s"Got text=$text")
    session.send("Hello world", last)
