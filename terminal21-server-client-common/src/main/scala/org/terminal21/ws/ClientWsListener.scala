package org.terminal21.ws

import scala.util.Using.Releasable

case class ClientWsListener[A](listener: ReliableClientWsListener, receivedIterator: Iterator[A], send: A => Unit):
  def transform[B](transformer: Transformer[A, B]): ClientWsListener[B] =
    ClientWsListener[B](listener, receivedIterator.map(transformer.transform), b => send(transformer.reverse(b)))

object ClientWsListener:
  given Releasable[ClientWsListener[_]] = _.listener.close()
