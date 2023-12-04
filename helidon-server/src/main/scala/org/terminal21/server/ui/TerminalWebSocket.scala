package org.terminal21.server.ui

import functions.fibers.FiberExecutor
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.server.json.*
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.ui.std.json.{Header1, Paragraph}

// websocket: https://helidon.io/docs/v4/#/se/websocket
class TerminalWebSocket(fiberExecutor: FiberExecutor, sessionsService: ServerSessionsService) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private def continuouslyRespond(session: WsSession, last: Boolean, sessionId: String) =
    fiberExecutor.submit:
      DoWhileSessionOpen.doWhileSessionOpen:
        val sessionState = sessionsService.sessionState(sessionId)
        for response <- sessionState.responses do session.send(response.asJson.noSpaces, last)
        sessionState.waitChange()

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    logger.info(s"Received json: $text")
    WsRequest.decoder(text) match
      case Right(WsRequest("init", Some(SessionId(sessionId)))) =>
        continuouslyRespond(session, last, sessionId)
        logger.info("init processed successfully")
      case x                                                    =>
        logger.error(s"Invalid request : $x")

trait TerminalWebSocketBeans(fiberExecutor: FiberExecutor):
  def sessionsService: ServerSessionsService
  lazy val terminalWebSocket = new TerminalWebSocket(fiberExecutor, sessionsService)
