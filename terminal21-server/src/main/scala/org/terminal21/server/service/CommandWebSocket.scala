package org.terminal21.server.service

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.model.{ClientToServer, CommandEvent, SubscribeTo}
import org.terminal21.server.ui.WsSessionOps
import org.terminal21.utils.ErrorLogger

class CommandWebSocket(sessionsService: ServerSessionsService) extends WsListener:
  private val logger      = LoggerFactory.getLogger(getClass.getName)
  private val errorLogger = new ErrorLogger(logger)

  override def onOpen(wsSession: WsSession): Unit =
    errorLogger.logErrors:
      logger.info(s"Session $wsSession opened")
      wsSession.send("init", true)

  override def onClose(wsSession: WsSession, status: Int, reason: String): Unit =
    logger.info(s"Session $wsSession closed")

  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    errorLogger.logErrors:
      decode[ClientToServer](text) match
        case Right(SubscribeTo(session)) =>
          logger.info(s"Command subscribes to events of session ${session.id}")
          sessionsService.notifyMeOnSessionEvents(session): event =>
            WsSessionOps.returnTrueIfSessionIsNotClosed:
              wsSession.send(event.asJson.noSpaces, true)
        case Left(e)                     => throw new IllegalStateException(s"Invalid json : json = $text  error = $e")

trait CommandWebSocketBeans:
  def sessionsService: ServerSessionsService
//  def fiberExecutor: FiberExecutor
//  lazy val commandWebSocketListener = ReliableServerWsListener.server(fiberExecutor)
  lazy val commandWebSocket = new CommandWebSocket(sessionsService)
