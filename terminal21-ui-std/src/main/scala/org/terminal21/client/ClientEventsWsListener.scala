package org.terminal21.client

import functions.fibers.FiberExecutor
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.common.buffers.BufferData
import io.helidon.webclient.websocket.WsClient
import org.slf4j.LoggerFactory
import org.terminal21.model.{ClientToServer, CommandEvent, Session, SubscribeTo}
import org.terminal21.ws.ReliableClientWsListener

import java.util.UUID

class ClientEventsWsListener(wsClient: WsClient, session: ConnectedSession, executor: FiberExecutor):
  private val logger         = LoggerFactory.getLogger(getClass)
  private val id             = UUID.randomUUID().toString
  private val eventsListener = ReliableClientWsListener.client(id, wsClient, "/api/command-ws", executor, pingEveryMs = 500).transform(decoder, encoder)

  private def decoder(buf: BufferData): Either[Error, CommandEvent] = decode[CommandEvent](new String(buf.readBytes(), "UTF-8"))
  private def encoder(cts: ClientToServer): BufferData              =
    val j = cts.asJson.noSpaces
    BufferData.create(j.getBytes("UTF-8"))

  def start(): Unit =
    executor.submit:
      val send = eventsListener.send
      val it   = eventsListener.receivedIterator
      send(SubscribeTo(session.session))
      for msg <- it do
        msg match
          case Left(e)      =>
            logger.error(s"An invalid json was received as an event. error = $e")
          case Right(event) =>
            executor.submit:
              session.fireEvent(event)

  def close(): Unit =
    eventsListener.close()
