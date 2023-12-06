package org.terminal21.client.ws

import io.helidon.websocket.{WsListener, WsSession}

import java.util.concurrent.LinkedBlockingQueue

class TwoWayWsListener[A, B](toString: A => String, fromString: String => B) extends WsListener:
  private val toSend    = new LinkedBlockingQueue[A](64)
  private val toReceive = new LinkedBlockingQueue[B](64)

  def senderAndReceiver: SenderAndReceiver[A, B] = new SenderAndReceiver(toSend, toReceive)

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    val b = fromString(text)
    toReceive.put(b)

  override def onOpen(session: WsSession): Unit =
    while true do
      val msg = toSend.take()
      session.send(toString(msg), true)

class SenderAndReceiver[A, B](toSend: LinkedBlockingQueue[A], toReceive: LinkedBlockingQueue[B]):
  def send(a: A): Unit = toSend.put(a)
  def receive: B       = toReceive.take()
