package org.terminal21.ws

import functions.fibers.FiberExecutor
import io.helidon.common.buffers.BufferData
import io.helidon.websocket.WsSession

import scala.util.Using.Releasable

abstract class ReliableServerWsListener(fiberExecutor: FiberExecutor) extends AbstractWsListener:

  protected def receive(id: String, data: BufferData): Unit

  override def onMessage(wsSession: WsSession, data: BufferData, last: Boolean): Unit =
    val len = data.read()
    val id  = data.readString(len)
    receive(id, data)

  override def onClose(wsSession: WsSession, status: Int, reason: String): Unit =
    logger.info(s"Server session $wsSession closed with status $status and reason $reason")

  override def onError(wsSession: WsSession, t: Throwable): Unit =
    logger.error(s"Server session $wsSession had an error", t)

  override def onOpen(wsSession: WsSession): Unit = ()

  def close(): Unit = ()

case class ServerWsListener(listener: ReliableServerWsListener, sender: BufferData => Unit)

object ServerWsListener:
  given Releasable[ServerWsListener] = _.listener.close()

object ReliableServerWsListener:
  def server(fiberExecutor: FiberExecutor)(receiver: (String, BufferData) => Unit) =
    val listener = new ReliableServerWsListener(fiberExecutor):
      override protected def receive(id: String, data: BufferData): Unit = receiver(id, data)

    ServerWsListener(listener, sender = data => ???)
