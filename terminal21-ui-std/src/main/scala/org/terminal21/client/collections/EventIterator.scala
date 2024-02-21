package org.terminal21.client.collections

class EventIterator[A](it: Iterator[A]) extends Iterator[A]:
  override def hasNext: Boolean = it.hasNext
  override def next(): A        = it.next()

  def lastOption: Option[A] =
    var last = Option.empty[A]
    while hasNext do last = Some(next())
    last

object EventIterator:
  def apply[A](items: A*): EventIterator[A] = new EventIterator(Iterator(items*))
