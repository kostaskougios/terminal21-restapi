package org.terminal21.client.ws

import functions.fibers.FiberExecutor
import io.helidon.websocket.{WsListener, WsSession}

import java.util.concurrent.LinkedBlockingQueue

class TwoWayWsListener(fiberExecutor: FiberExecutor) extends WsListener:
  private val toSend    = new LinkedBlockingQueue[String](64)
  private val toReceive = new LinkedBlockingQueue[String](64)

  def senderAndReceiver: SenderAndReceiver = new SenderAndReceiver(toSend, toReceive)

  override def onMessage(session: WsSession, text: String, last: Boolean): Unit =
    toReceive.put(text)

  override def onOpen(session: WsSession): Unit =
    fiberExecutor.submit:
      while true do
        val msg = toSend.take()
        session.send(msg, true)

class SenderAndReceiver(toSend: LinkedBlockingQueue[String], toReceive: LinkedBlockingQueue[String]):
  def send(a: String): Unit = toSend.put(a)
  def receive: String       = toReceive.take()
