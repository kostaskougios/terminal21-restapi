package org.terminal21.ws

import io.helidon.common.buffers.BufferData
import org.terminal21.collections.LazyBlockingIterator

import scala.util.Using.Releasable

case class ServerWsListener[A](
    listener: ReliableServerWsListener,
    dataIterator: LazyBlockingIterator[ServerValue[BufferData]],
    receivedIterator: Iterator[A],
    send: A => Unit
)

object ServerWsListener:
  extension (l: ServerWsListener[ServerValue[BufferData]])
    def transform[B](transformer: Transformer[BufferData, B]): ServerWsListener[ServerValue[B]] =
      ServerWsListener(
        l.listener,
        l.dataIterator,
        l.receivedIterator.map(sv => ServerValue(sv.id, transformer.transform(sv.value))),
        sv => l.send(ServerValue(sv.id, transformer.reverse(sv.value)))
      )

  given Releasable[ServerWsListener[_]] = s =>
    s.listener.close()
    s.dataIterator.close()
