package org.terminal21.ws

import functions.fibers.{Fiber, FiberExecutor}
import io.helidon.common.buffers.BufferData
import io.helidon.webclient.websocket.WsClient
import io.helidon.websocket.{WsCloseCodes, WsSession}
import org.terminal21.collections.{LazyBlockingIterator, ProducerConsumerCollections}

import java.io.UncheckedIOException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, LinkedBlockingQueue}
import scala.annotation.tailrec

abstract class ReliableClientWsListener(id: String, wsClient: WsClient, remotePath: String, fiberExecutor: FiberExecutor, pingEveryMs: Long)
    extends AbstractWsListener:
  private val idDataStr = id.getBytes("UTF-8")
  private def idData    = BufferData.create(BufferData.create(Array[Byte](idDataStr.length.toByte)), BufferData.create(idDataStr))
  if (idDataStr.length > 255) throw new IllegalArgumentException("id must be less than 255 bytes")

  private val toSendQueue              = new LinkedBlockingQueue[BufferData](64)
  @volatile private var wsSession      = Option.empty[WsSession]
  @volatile private var wsSessionReady = new CountDownLatch(1)
  private val isRunning                = new AtomicBoolean(true)
  setupPeriodicalPing()
  private val senderFiber              = setupSender()
  connect()

  def close(): Unit =
    isRunning.set(false)
    senderFiber.interrupt()
    tryOnSocketClosedIgnore(closeWsSession())

  private def closeWsSession(): Unit =
    for s <- wsSession do s.close(WsCloseCodes.NORMAL_CLOSE, "closing connection normally")

  def connect(): Unit =
    if isRunning.get() then
      wsSessionReady.countDown()
      wsSessionReady = new CountDownLatch(1)
      errorLogger.tryForeverLogErrors:
        wsClient.connect(remotePath, this)

  protected def setupPeriodicalPing(): Unit =
    fiberExecutor.submit:
      while isRunning.get() do
        errorLogger.logErrors:
          for (s <- wsSession)
            tryOnSocketClosedReconnect:
              s.ping(BufferData.empty())
        Thread.sleep(pingEveryMs)

  protected def receive(data: BufferData): Unit

  private def setupSender(): Fiber[Unit] =
    fiberExecutor.submit:
      while isRunning.get() do
        val d = toSendQueue.take()

        @tailrec def sendIt(): Unit =
          wsSessionReady.await()
          val wasSend = wsSession.exists: s =>
            tryOnSocketClosedReconnect:
              s.send(d, true)

          if !wasSend then sendIt()

        sendIt()

  def send(data: BufferData): Unit =
    val d = BufferData.create(idData.copy(), data)
    toSendQueue.put(d)

  override def onMessage(wsSession: WsSession, data: BufferData, last: Boolean): Unit =
    errorLogger.logErrors:
      receive(data)

  override def onClose(wsSession: WsSession, status: Int, reason: String): Unit =
    logger.info(s"session $wsSession closed with status $status and reason $reason, reconnecting ....")
    connect()

  override def onError(wsSession: WsSession, t: Throwable): Unit =
    logger.error(s"an error occurred for $wsSession", t)

  override def onOpen(wsSession: WsSession): Unit =
    errorLogger.logErrors:
      this.wsSession = Some(wsSession)
      wsSessionReady.countDown()
      send(BufferData.empty()) // make sure the server has our id

  private def tryOnSocketClosedReconnect(f: => Unit): Boolean =
    try
      f
      true
    catch
      case _: UncheckedIOException | _: IllegalStateException =>
        //        logger.info("Socket closed, reconnecting...")
        connect()
        false

object ReliableClientWsListener:
  def client(
      id: String,
      wsClient: WsClient,
      remotePath: String,
      fiberExecutor: FiberExecutor,
      pingEveryMs: Long = 1000
  ): ClientWsListener[BufferData, BufferData] =
    val (it, producer) = ProducerConsumerCollections.lazyIterator[BufferData]()
    val listener       = new ReliableClientWsListener(id, wsClient, remotePath, fiberExecutor, pingEveryMs):
      override protected def receive(data: BufferData): Unit = producer(data)
    ClientWsListener(listener, it, it, listener.send)
