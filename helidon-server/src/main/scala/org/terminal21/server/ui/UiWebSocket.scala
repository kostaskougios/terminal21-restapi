package org.terminal21.server.ui

import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.server.json.{Initialized, WsRequest, WsResponse}

// websocket: https://helidon.io/docs/v4/#/se/websocket
class UiWebSocket extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  override def onMessage(session: WsSession, text: String, last: Boolean) =
    logger.info(s"Received json: $text")
    WsRequest.decoder(text) match
      case Right(WsRequest("init", None)) =>
        val res  = Initialized("ok")
        val json = WsResponse.encoder(res).noSpaces
        logger.info(s"Got an init, responding with $json")
        session.send(json, last)
      case x                              =>
        logger.error(s"Invalid request : $x")
