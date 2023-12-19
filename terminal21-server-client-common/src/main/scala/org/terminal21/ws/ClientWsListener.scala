package org.terminal21.ws

import io.helidon.common.buffers.BufferData
import org.terminal21.collections.LazyBlockingIterator

import scala.util.Using.Releasable

case class ClientWsListener[A](
    listener: ReliableClientWsListener,
    dataIterator: LazyBlockingIterator[BufferData],
    receivedIterator: Iterator[A],
    send: A => Unit
):
  def transform[B](transformer: Transformer[A, B]): ClientWsListener[B] =
    ClientWsListener[B](listener, dataIterator, receivedIterator.map(transformer.transform), b => send(transformer.reverse(b)))

object ClientWsListener:
  given Releasable[ClientWsListener[_]] =
    l =>
      l.listener.close()
      l.dataIterator.close()
