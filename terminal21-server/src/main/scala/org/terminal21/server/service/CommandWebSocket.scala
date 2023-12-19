package org.terminal21.server.service

import functions.fibers.FiberExecutor
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.helidon.common.buffers.BufferData
import org.slf4j.LoggerFactory
import org.terminal21.model.{ClientToServer, CommandEvent, SubscribeTo}
import org.terminal21.ws.{ReliableServerWsListener, ServerValue}

class CommandWebSocket(executor: FiberExecutor, sessionsService: ServerSessionsService):
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private def decoder(buf: BufferData): ClientToServer = {
    val json = new String(buf.readBytes(), "UTF-8")
    decode[ClientToServer](json) match
      case Right(msg) => msg
      case Left(e)    => throw new IllegalStateException(s"Invalid json : json = $json  error = $e")
  }

  private def encoder(event: CommandEvent): BufferData =
    val j = event.asJson.noSpaces
    BufferData.create(j.getBytes("UTF-8"))

  val commandWebSocketListener = ReliableServerWsListener
    .server(executor)
    .transformValue(
      decoder,
      encoder
    )

  start()

  def start(): Unit =
    executor.submit:
      val send = commandWebSocketListener.send
      for sv <- commandWebSocketListener.receivedIterator do
        sv.value match
          case SubscribeTo(session) =>
            logger.info(s"Command subscribes to events of session ${session.id}")
            sessionsService.notifyMeOnSessionEvents(session): event =>
              val e = ServerValue(sv.id, event)
              send(e)
              true

trait CommandWebSocketBeans:
  def sessionsService: ServerSessionsService
  def fiberExecutor: FiberExecutor
  lazy val commandWebSocket = new CommandWebSocket(fiberExecutor, sessionsService)
