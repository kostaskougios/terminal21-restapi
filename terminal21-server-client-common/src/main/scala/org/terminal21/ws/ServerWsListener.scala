package org.terminal21.ws

import io.helidon.common.buffers.BufferData
import org.terminal21.collections.LazyBlockingIterator

import scala.util.Using.Releasable

case class ServerWsListener[R, S](
    listener: ReliableServerWsListener,
    dataIterator: LazyBlockingIterator[ServerValue[BufferData]],
    receivedIterator: Iterator[R],
    send: S => Unit
):
  def close(): Unit =
    listener.close()
    dataIterator.close()

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

    def transformValue[NR, NS](
        receiveTransformer: BufferData => NR,
        sendTransformer: NS => BufferData
    ): ServerWsListener[ServerValue[NR], ServerValue[NS]] =
      ServerWsListener(
        l.listener,
        l.dataIterator,
        l.receivedIterator.map(sv => sv.copy(value = receiveTransformer(sv.value))),
        sv => l.send(sv.copy(value = sendTransformer(sv.value)))
      )

  given Releasable[ServerWsListener[_, _]] = _.close()
