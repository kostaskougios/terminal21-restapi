package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.slf4j.LoggerFactory
import org.terminal21.model.{CommandEvent, Session, SubscribeTo}

class EventsWsListener(session: ConnectedSession) extends WsListener:
  private val logger = LoggerFactory.getLogger(getClass)

  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    decode[CommandEvent](text) match
      case Left(e)      =>
        logger.error(s"An invalid json was received as an event. json=$text , error = $e")
      case Right(event) =>
        logger.info(s"Firing event $event")
        session.fireEvent(event)

  override def onOpen(wsSession: WsSession): Unit =
    wsSession.send(SubscribeTo(session.session).asJson.noSpaces, true)
