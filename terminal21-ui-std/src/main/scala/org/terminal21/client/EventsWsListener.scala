package org.terminal21.client

import functions.fibers.FiberExecutor
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.common.buffers.BufferData
import io.helidon.webclient.websocket.WsClient
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.model.{ClientToServer, CommandEvent, Session, SubscribeTo}
import org.terminal21.utils.ErrorLogger

class EventsWsListener(wsClient: WsClient, session: ConnectedSession, fiberExecutor: FiberExecutor) extends WsListener:
  private val logger      = LoggerFactory.getLogger(getClass)
  private val errorLogger = new ErrorLogger(logger)

  def connect(): Unit =
    wsClient.connect("/api/command-ws", this)

  private def setupPeriodicalPing(wsSession: WsSession) =
    fiberExecutor.submit:
      while true do
        errorLogger.logErrors:
          Thread.sleep(2000)
          wsSession.ping(BufferData.empty())

  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    errorLogger.logErrors:
      if text == "init" then
        logger.info("init received, subscribing to events")
        setupPeriodicalPing(wsSession)
        val s: ClientToServer = SubscribeTo(session.session)
        val j                 = s.asJson.noSpaces
        wsSession.send(j, true)
      else
        decode[CommandEvent](text) match
          case Left(e)      =>
            logger.error(s"An invalid json was received as an event. json=$text , error = $e")
          case Right(event) =>
            session.fireEvent(event)

  override def onOpen(wsSession: WsSession): Unit =
    logger.info(s"session $wsSession opened")

  override def onClose(wsSession: WsSession, status: Int, reason: String): Unit =
    logger.info(s"client session $wsSession closed with status $status and reason $reason")

  override def onError(wsSession: WsSession, t: Throwable): Unit =
    logger.error(s"client session $wsSession closed with this error.", t)
