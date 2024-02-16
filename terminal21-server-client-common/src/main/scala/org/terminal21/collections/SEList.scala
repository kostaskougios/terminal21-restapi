package org.terminal21.collections

import java.util.concurrent.CountDownLatch

class SEList[A]:
  @volatile private var currentNode: NormalNode[A] = NormalNode(None, EndNode)
  def iterator: Iterator[A]                        = new SEBlockingIterator(currentNode)

  def poisonPill(): Unit =
    synchronized:
      currentNode.valueAndNext = (None, PoisonPillNode)
      currentNode.latch.countDown()

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
  override def hasNext: Boolean =
    currentNode.waitValue()
    val v = currentNode.valueAndNext._2
    if v == PoisonPillNode then false else true

  override def next(): A =
    hasNext
    val v = currentNode.value
    currentNode = currentNode.next
    v

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
