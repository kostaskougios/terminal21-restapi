package org.terminal21.collections

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import scala.annotation.tailrec

class LazyBlockingIterator[A](q: LinkedBlockingQueue[A]) extends Iterator[A]:
  private val open                      = new AtomicBoolean(true)
  override def hasNext: Boolean         = true
  @tailrec override final def next(): A =
    q.poll(10, TimeUnit.MILLISECONDS) match
      case null =>
        if !open.get() then throw new NoSuchElementException()
        next()
      case e    => e

  def close(): Unit =
    open.set(false)

object ProducerConsumerCollections:
  def lazyIterator[A](initialSize: Int = 64): (LazyBlockingIterator[A], A => Unit) =
    val queue = new LinkedBlockingQueue[A](initialSize)
    val it    = new LazyBlockingIterator[A](queue)
    (it, a => queue.put(a))
