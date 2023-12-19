package org.terminal21.ws

import io.helidon.common.buffers.BufferData
import org.terminal21.collections.LazyBlockingIterator

import scala.util.Using.Releasable

case class ClientWsListener[R, S](
    listener: ReliableClientWsListener,
    dataIterator: LazyBlockingIterator[BufferData],
    receivedIterator: Iterator[R],
    send: S => Unit
):
  def transform[NR, NS](receiveTransformer: R => NR, sendTransformer: NS => S): ClientWsListener[NR, NS] =
    ClientWsListener(listener, dataIterator, receivedIterator.map(receiveTransformer), b => send(sendTransformer(b)))

object ClientWsListener:
  given Releasable[ClientWsListener[_, _]] =
    l =>
      l.listener.close()
      l.dataIterator.close()
