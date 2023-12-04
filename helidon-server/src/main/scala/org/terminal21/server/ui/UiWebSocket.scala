package org.terminal21.server.ui

import functions.fibers.FiberExecutor
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.server.json.*
import org.terminal21.ui.std.json.{Header1, Paragraph}

// websocket: https://helidon.io/docs/v4/#/se/websocket
class UiWebSocket(fiberExecutor: FiberExecutor) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private def continuouslyRespond(session: WsSession, last: Boolean, sessionId: String) =
    fiberExecutor.submit:
      var c = 0
      DoWhileSessionOpen.doWhileSessionOpen:
        val res  = Std(Seq(Header1("Notes"), Paragraph(s"$c : Hello world! $sessionId")))
        val json = WsResponse.encoder(res).noSpaces
        logger.info(s"responding with $json")
        session.send(json, last)
        Thread.sleep(2000)
        c += 1

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    logger.info(s"Received json: $text")
    WsRequest.decoder(text) match
      case Right(WsRequest("init", Some(SessionId(sessionId)))) =>
        continuouslyRespond(session, last, sessionId)
        logger.info("init processed successfully")
      case x                                                    =>
        logger.error(s"Invalid request : $x")

trait UiWebSocketBeans(fiberExecutor: FiberExecutor):
  lazy val uiWebSocket = new UiWebSocket(fiberExecutor)
