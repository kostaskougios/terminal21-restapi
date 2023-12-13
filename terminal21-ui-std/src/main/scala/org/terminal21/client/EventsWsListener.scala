package org.terminal21.client

import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.terminal21.model.{CommandEvent, Session, SubscribeTo}

class EventsWsListener(session: ConnectedSession) extends WsListener:
  override def onMessage(wsSession: WsSession, text: String, last: Boolean): Unit =
    decode[CommandEvent](text) match
      case Left(e)      =>
        println(s"An invalid json was received as an event. json=$text , error = $e")
      case Right(event) =>
        session.fireEvent(event)

  override def onOpen(wsSession: WsSession): Unit =
    wsSession.send(SubscribeTo(session.session).asJson.noSpaces, true)
