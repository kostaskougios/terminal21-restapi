package org.terminal21.ws

import functions.fibers.FiberExecutor
import io.helidon.common.buffers.BufferData
import io.helidon.websocket.WsSession
import org.terminal21.collections.{LazyBlockingIterator, ProducerConsumerCollections}

import scala.collection.concurrent.TrieMap

abstract class ReliableServerWsListener(fiberExecutor: FiberExecutor) extends AbstractWsListener:
  private val perClientIdWsSession = TrieMap.empty[String, WsSession]

  protected def receive(id: String, data: BufferData): Unit

  def hasClientId(id: String): Boolean = perClientIdWsSession.contains(id)

  def send(id: String, data: BufferData): Unit =
    val wsSession = perClientIdWsSession.getOrElse(id, throw new IllegalArgumentException(s"No client with id = $id has a session"))
    wsSession.send(data, true)

  override def onMessage(wsSession: WsSession, data: BufferData, last: Boolean): Unit =
    fiberExecutor.submit:
      errorLogger.logErrors:
        if data.available() > 0 then
          val len    = data.read()
          val strDat = new Array[Byte](len)
          data.read(strDat)
          val id     = new String(strDat, "UTF-8")
          perClientIdWsSession.put(id, wsSession)
          if data.available() > 0 then receive(id, data)
        else logger.warn(s"Received empty message for $wsSession")

  override def onClose(wsSession: WsSession, status: Int, reason: String): Unit =
    logger.info(s"Server session $wsSession closed with status $status and reason $reason")

  override def onError(wsSession: WsSession, t: Throwable): Unit =
    logger.error(s"Server session $wsSession had an error", t)

  def close(): Unit =
    perClientIdWsSession.clear()

case class ServerValue[A](id: String, value: A)

object ReliableServerWsListener:
  def server(fiberExecutor: FiberExecutor): ServerWsListener[ServerValue[BufferData]] =
    val (it, producer) = ProducerConsumerCollections.lazyIterator[ServerValue[BufferData]]()
    val listener       = new ReliableServerWsListener(fiberExecutor):
      override protected def receive(id: String, data: BufferData): Unit = producer(ServerValue(id, data))

    ServerWsListener(listener, it, it, sv => listener.send(sv.id, sv.value))
