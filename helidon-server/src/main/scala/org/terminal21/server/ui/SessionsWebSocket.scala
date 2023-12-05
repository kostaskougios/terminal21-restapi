package org.terminal21.server.ui

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.server.json.*
import org.terminal21.server.service.ServerSessionsService

// websocket: https://helidon.io/docs/v4/#/se/websocket
class SessionsWebSocket(sessionsService: ServerSessionsService) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private def continuouslyRespond(session: WsSession, last: Boolean) =
    sessionsService.notifyMeWhenSessionsChange: allSessions =>
      DoWhileSessionOpen.returnTrueWhileSessionOpen:
        val sessions = allSessions.map(_.copy(secret = "***"))
        val json     = sessions.asJson.noSpaces
        logger.info(s"Sending sessions = $json to $session")
        session.send(json, last)

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    logger.info(s"Received json: $text")
    WsRequest.decoder(text) match
      case Right(WsRequest("sessions", None)) =>
        continuouslyRespond(session, last)
        logger.info("sessions processed successfully")
      case x                                  =>
        logger.error(s"Invalid request : $x")

trait SessionsWebSocketBeans:
  def sessionsService: ServerSessionsService
  lazy val sessionsWebSocket = new SessionsWebSocket(sessionsService)
