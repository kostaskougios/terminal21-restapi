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
import org.terminal21.ui.std.ServerJson
import org.terminal21.utils.ErrorLogger

// websocket: https://helidon.io/docs/v4/#/se/websocket
class SessionsWebSocket(sessionsService: ServerSessionsService) extends WsListener:
  private val logger      = LoggerFactory.getLogger(getClass.getName)
  private val errorLogger = new ErrorLogger(logger)

  private def continuouslyRespond(wsSession: WsSession): Unit =
    sessionsService.notifyMeWhenSessionsChange: allSessions =>
      WsSessionOps.returnTrueIfSessionIsNotClosed:
        sendSessions(wsSession, allSessions)

    sessionsService.notifyMeWhenSessionChanges: (session, sessionState, changeOpt) =>
      WsSessionOps.returnTrueIfSessionIsNotClosed:
        changeOpt match
          case None         => sendSessionState(wsSession, session, sessionState)
          case Some(change) => sendSessionStateChange(wsSession, session, change)

  private def sendSessionState(wsSession: WsSession, session: Session, sessionState: SessionState): Unit =
    val response = StateWsResponse(session.hideSecret, sessionState.json).asJson.noSpaces
    logger.info(s"$wsSession: Sending session state response $response")
    wsSession.send(response, true)

  private def sendSessionStateChange(wsSession: WsSession, session: Session, change: ServerJson): Unit =
    val response = StateChangeWsResponse(session.hideSecret, change).asJson.noSpaces
    logger.info(s"$wsSession: Sending session change state response $response")
    wsSession.send(response, true)

  private def sendSessions(wsSession: WsSession, allSessions: Seq[Session]): Unit =
    val sessions = allSessions.map(_.hideSecret).sortBy(_.name)
    val json     = SessionsWsResponse(sessions).asJson.noSpaces
    logger.info(s"$wsSession: Sending sessions $json")
    wsSession.send(json, true)

  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    logger.info(s"$wsSession: Received json: $text , last = $last")
    errorLogger.logErrors:
      WsRequest.decoder(text) match
        case Right(WsRequest("sessions", None))                                 =>
          continuouslyRespond(wsSession)
          logger.info(s"$wsSession: sessions processed successfully")
        case Right(WsRequest(eventName, Some(event: UiEvent)))                  =>
          logger.info(s"$wsSession: Received event $eventName = $event")
          sessionsService.addEvent(event)
        case Right(WsRequest("ping", None))                                     =>
          logger.info(s"$wsSession: ping received")
        case Right(WsRequest("close-session", Some(CloseSession(sessionId))))   =>
          val session = sessionsService.sessionById(sessionId)
          sessionsService.terminateSession(session)
        case Right(WsRequest("remove-session", Some(RemoveSession(sessionId)))) =>
          val session = sessionsService.sessionById(sessionId)
          sessionsService.removeSession(session)
        case x                                                                  =>
          logger.error(s"Invalid request : $x")

  override def onOpen(wsSession: WsSession): Unit =
    logger.info(s"session $wsSession opened")

  override def onClose(wsSession: WsSession, status: Int, reason: String): Unit =
    logger.info(s"Session $wsSession closed with status $status and reason=$reason.")

trait SessionsWebSocketBeans:
  def sessionsService: ServerSessionsService
  lazy val sessionsWebSocket = new SessionsWebSocket(sessionsService)
