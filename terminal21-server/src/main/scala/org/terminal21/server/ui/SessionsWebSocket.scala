package org.terminal21.server.ui

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.model.Session
import org.terminal21.server.json.*
import org.terminal21.server.model.SessionState
import org.terminal21.server.service.ServerSessionsService

// websocket: https://helidon.io/docs/v4/#/se/websocket
class SessionsWebSocket(sessionsService: ServerSessionsService) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private def continuouslyRespond(wsSession: WsSession, last: Boolean): Unit =
    sessionsService.notifyMeWhenSessionsChange: allSessions =>
      WsSessionOps.returnTrueWhileSessionOpen:
        sendSessions(wsSession, last, allSessions)

    sessionsService.notifyMeWhenSessionChanges: (session, sessionState) =>
      WsSessionOps.returnTrueWhileSessionOpen:
        sendSessionState(wsSession, last, session, sessionState)

  private def sendSessionState(wsSession: WsSession, last: Boolean, session: Session, sessionState: SessionState): Unit =
    val response = StateWsResponse(session.hideSecret, sessionState.json.asJson).asJson.noSpaces
    logger.info(s"Sending session state response $response")
    wsSession.send(response, last)

  private def sendSessions(wsSession: WsSession, last: Boolean, allSessions: Seq[Session]): Unit =
    val sessions = allSessions.map(_.hideSecret).sortBy(_.name)
    val json     = SessionsWsResponse(sessions).asJson.noSpaces
    logger.info(s"Sending sessions $json")
    wsSession.send(json, last)

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    logger.info(s"Received json: $text")
    WsRequest.decoder(text) match
      case Right(WsRequest("sessions", None))                =>
        continuouslyRespond(session, last)
        logger.info("sessions processed successfully")
      case Right(WsRequest(eventName, Some(event: UiEvent))) =>
        logger.info(s"Received event $eventName = $event")
        sessionsService.addEvent(event)
      case x                                                 =>
        logger.error(s"Invalid request : $x")

trait SessionsWebSocketBeans:
  def sessionsService: ServerSessionsService
  lazy val sessionsWebSocket = new SessionsWebSocket(sessionsService)
