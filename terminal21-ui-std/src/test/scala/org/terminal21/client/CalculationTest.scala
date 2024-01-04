package org.terminal21.client

import functions.fibers.FiberExecutor
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

import java.util.concurrent.atomic.AtomicInteger
import org.scalatest.concurrent.Eventually.*

class CalculationTest extends AnyFunSuiteLike:
  given executor: FiberExecutor      = FiberExecutor()
  def testCalc(i: Int)               = i + 1
  def testCalcString(i: Int): String = (i + 10).toString

  test("calculates"):
    val calc = Calculation(testCalc, () => (), _ => (), Nil)
    calc(1) should be(2)

  test("calls the ui updater with the calculated value"):
    val c    = new AtomicInteger(-1)
    val calc = Calculation(testCalc, () => (), i => c.set(i), Nil)
    calc(1)
    eventually:
      c.get() should be(2)

  test("notifies"):
    val c     = new AtomicInteger(-1)
    val calc2 = Calculation(testCalcString, () => (), i => c.set(i.toInt), Nil)
    val calc1 = Calculation(testCalc, () => (), _ => ()).notifyCalc(calc2)
    calc1(1)
    eventually:
      c.get() should be(12)
