package org.terminal21.collections

import functions.fibers.FiberExecutor
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class SEListTest extends AnyFunSuiteLike:
  val executor = FiberExecutor()

  test("when empty, it.hasNext should wait"):
    val l  = SEList[Int]()
    val it = l.iterator
    l.poisonPill()
    it.toList should be(Nil)

  test("when empty with 2 iterators"):
    val l   = SEList[Int]()
    val it1 = l.iterator
    val it2 = l.iterator
    l.poisonPill()
    it1.toList should be(Nil)
    it2.toList should be(Nil)

  test("with 1 item"):
    val l  = SEList[Int]()
    val it = l.iterator
    l.add(1)
    l.poisonPill()
    it.toList should be(List(1))

  test("with 2 items"):
    val l  = SEList[Int]()
    val it = l.iterator
    l.add(1)
    l.add(2)
    l.poisonPill()
    it.toList should be(List(1, 2))

  test("with 2 items and 2 iterators"):
    val l   = SEList[Int]()
    val it1 = l.iterator
    val it2 = l.iterator
    l.add(1)
    l.add(2)
    l.poisonPill()
    it1.toList should be(List(1, 2))
    it2.toList should be(List(1, 2))

  test("iterator after added items"):
    val l  = SEList[Int]()
    l.add(1)
    val it = l.iterator
    l.add(2)
    l.poisonPill()
    it.toList should be(List(2))

  test("hasNext & next()"):
    val l  = SEList[Int]()
    val it = l.iterator
    l.add(1)
    l.add(2)
    l.poisonPill()
    it.hasNext should be(true)
    it.next() should be(1)
    it.hasNext should be(true)
    it.next() should be(2)
    it.hasNext should be(false)
    an[NoSuchElementException] should be thrownBy (it.next())

  test("multiple iterators and multi threading"):
    val l         = SEList[Int]()
    val iterators = for _ <- 1 to 1000 yield
      val it = l.iterator
      executor.submit:
        it.toList

    for i <- 1 to 100 do
      Thread.sleep(1)
      l.add(i)

    l.poisonPill()

    val expected = (1 to 100).toList
    for f <- iterators do f.get() should be(expected)
