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

  test("when empty with 2 starters"):
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
