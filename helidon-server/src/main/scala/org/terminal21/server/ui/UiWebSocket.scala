package org.terminal21.server.ui

import functions.fibers.FiberExecutor
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.server.json.*

// websocket: https://helidon.io/docs/v4/#/se/websocket
class UiWebSocket(fiberExecutor: FiberExecutor) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    logger.info(s"Received json: $text")
    WsRequest.decoder(text) match
      case Right(WsRequest("init", None)) =>
        fiberExecutor.submit:
          try
            var c = 0
            while true do
              val res  = Std(Seq(Paragraph(s"$c : Hello world!")))
              val json = WsResponse.encoder(res).noSpaces
              logger.info(s"Got an init, responding with $json")
              session.send(json, last)
              Thread.sleep(2000)
              c += 1
          catch
            case t: Throwable =>
              logger.error("fiber error occurred", t)
      case x                              =>
        logger.error(s"Invalid request : $x")
