package org.terminal21.client.ws

import functions.fibers.FiberExecutor
import io.helidon.websocket.{WsListener, WsSession}
import org.terminal21.client.ws.TwoWayWsListener.PoisonPill

import java.util.concurrent.LinkedBlockingQueue
import scala.annotation.tailrec
import scala.util.Using.Releasable

class TwoWayWsListener(fiberExecutor: FiberExecutor) extends WsListener:
  private val toSend    = new LinkedBlockingQueue[String](64)
  private val toReceive = new LinkedBlockingQueue[String](64)

  def senderAndReceiver: SenderAndReceiver = new SenderAndReceiver(toSend, toReceive)

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    toReceive.put(text)

  override def onOpen(session: WsSession): Unit =
    fiberExecutor.submit:
      @tailrec def cont(): Unit =
        val msg = toSend.take()
        if !msg.eq(PoisonPill) then
          session.send(msg, true)
          cont()
      cont()

  def close(): Unit = toSend.put(PoisonPill)

class SenderAndReceiver(toSend: LinkedBlockingQueue[String], toReceive: LinkedBlockingQueue[String]):
  def send(a: String): Unit = toSend.put(a)
  def receive: String       = toReceive.take()

object TwoWayWsListener:
  val PoisonPill = "##PoisonPill##"

  given Releasable[TwoWayWsListener] = twl => twl.close()
