package org.terminal21.server.service

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.model.{CommandEvent, SubscribeTo}

class CommandWebSocket(sessionsService: ServerSessionsService) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    decode[SubscribeTo](text) match
      case Left(e)                     => throw new IllegalStateException(s"Invalid json : json = $text  error = $e")
      case Right(SubscribeTo(session)) =>
        logger.info(s"Command subscribes to events of session ${session.id}")
        sessionsService.notifyMeOnSessionEvents(session): event =>
          wsSession.send(event.asJson.noSpaces, true)
          true

trait CommandWebSocketBeans:
  def sessionsService: ServerSessionsService
  lazy val commandWebSocket = new CommandWebSocket(sessionsService)
