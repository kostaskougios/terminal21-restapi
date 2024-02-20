package org.terminal21.collections

import java.util.concurrent.CountDownLatch

class SEList[A]:
  @volatile private var currentNode: NormalNode[A] = NormalNode(None, EndNode)

  /** @return
    *   A new iterator that only reads elements that are added before the iterator is created.
    */
  def iterator: SEBlockingIterator[A] = new SEBlockingIterator(currentNode)

  /** Add a poison pill to terminate all iterators.
    */
  def poisonPill(): Unit =
    synchronized:
      currentNode.valueAndNext = (None, PoisonPillNode)
      currentNode.latch.countDown()

  /** Adds an item that will be visible to all iterators that were created before this item was added.
    * @param item
    *   the item
    */
  def add(item: A): Unit =
    val cn = synchronized:
      val cn = currentNode
      if cn.valueAndNext._2 == PoisonPillNode then throw new IllegalStateException("Can't add items when the list has been poisoned.")
      val n  = NormalNode(None, currentNode.valueAndNext._2)
      currentNode.valueAndNext = (Some(item), n)
      currentNode = n
      cn
    cn.latch.countDown()

class SEBlockingIterator[A](@volatile var currentNode: NormalNode[A]) extends Iterator[A]:
  /** @return
    *   true if hasNext & next() will return immediately with the next value. This won't block.
    */
  def isNextAvailable: Boolean = currentNode.hasValue

  /** @return
    *   true if there is a next() but blocks otherwise till next() becomes available or we are at the end of the iterator.
    */
  override def hasNext: Boolean =
    currentNode.waitValue()
    val v = currentNode.valueAndNext._2
    if v == PoisonPillNode then false else true

  /** @return
    *   the next element or blocks until the next element becomes available
    */
  override def next(): A =
    if hasNext then
      val v = currentNode.value
      currentNode = currentNode.next
      v
    else throw new NoSuchElementException("next() called but there is no next element. The SEList has been poisoned and we reached the PoisonPill")

sealed trait Node[+A]
case object EndNode        extends Node[Nothing]
case object PoisonPillNode extends Node[Nothing]

case class NormalNode[A](@volatile var valueAndNext: (Option[A], Node[A])) extends Node[A]:
  val latch             = new CountDownLatch(1)
  def waitValue(): Unit = latch.await()

  def hasValue: Boolean   = valueAndNext._1.nonEmpty
  def value: A            = valueAndNext._1.get
  def next: NormalNode[A] = valueAndNext._2 match
    case nn: NormalNode[A] @unchecked => nn
    case _                            => throw new NoSuchElementException("next should be called only if hasValue is true")
