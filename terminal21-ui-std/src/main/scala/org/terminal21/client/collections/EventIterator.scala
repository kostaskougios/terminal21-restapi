package org.terminal21.client.collections

import org.terminal21.client.ConnectedSession

import scala.collection.AbstractIterator

class EventIterator[A](it: Iterator[A]) extends AbstractIterator[A]:
  override def hasNext: Boolean = it.hasNext
  override def next(): A        = it.next()

  def lastOption: Option[A] =
    var last = Option.empty[A]
    while hasNext do last = Some(next())
    last

  def lastOptionOrNoneIfSessionClosed(using session: ConnectedSession) =
    val v = lastOption
    if session.isClosed then None else v

object EventIterator:
  def apply[A](items: A*): EventIterator[A] = new EventIterator(Iterator(items*))
