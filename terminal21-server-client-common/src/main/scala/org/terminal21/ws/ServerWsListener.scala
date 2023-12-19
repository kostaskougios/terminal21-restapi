package org.terminal21.ws

import io.helidon.common.buffers.BufferData
import org.terminal21.collections.LazyBlockingIterator

import scala.util.Using.Releasable

case class ServerWsListener[R, S](
    listener: ReliableServerWsListener,
    dataIterator: LazyBlockingIterator[ServerValue[BufferData]],
    receivedIterator: Iterator[R],
    send: S => Unit
)

object ServerWsListener:
  extension (l: ServerWsListener[ServerValue[BufferData], ServerValue[BufferData]])
    def transform[NR, NS](
        receiveTransformer: ServerValue[BufferData] => NR,
        sendTransformer: NS => ServerValue[BufferData]
    ): ServerWsListener[NR, NS] =
      ServerWsListener(
        l.listener,
        l.dataIterator,
        l.receivedIterator.map(receiveTransformer),
        sv => l.send(sendTransformer(sv))
      )

  given Releasable[ServerWsListener[_, _]] = s =>
    s.listener.close()
    s.dataIterator.close()
