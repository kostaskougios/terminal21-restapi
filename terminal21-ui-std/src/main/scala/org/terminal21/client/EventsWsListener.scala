package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.helidon.websocket.{WsListener, WsSession}
import org.terminal21.model.{Session, SubscribeTo}

class EventsWsListener(session: Session) extends WsListener:
  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    println(s"EventsWsListener: Received $text")

  override def onOpen(wsSession: WsSession): Unit =
    wsSession.send(SubscribeTo(session).asJson.noSpaces, true)
