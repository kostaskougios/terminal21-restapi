package org.terminal21.collections

import java.util.concurrent.LinkedBlockingQueue

class LazyBlockingIterator[A](q: LinkedBlockingQueue[A]) extends Iterator[A]:
  override def hasNext: Boolean = true
  override def next(): A        =
    q.take()

object ProducerConsumerCollections:
  def lazyIterator[A](initialSize: Int = 64): (LazyBlockingIterator[A], A => Unit) =
    val queue = new LinkedBlockingQueue[A](initialSize)
    val it    = new LazyBlockingIterator[A](queue)
    (it, a => queue.put(a))
