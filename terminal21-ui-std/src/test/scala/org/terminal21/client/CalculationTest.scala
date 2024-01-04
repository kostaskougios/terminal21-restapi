package org.terminal21.client

import functions.fibers.FiberExecutor
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import org.scalatest.concurrent.Eventually.*

class CalculationTest extends AnyFunSuiteLike:
  given executor: FiberExecutor      = FiberExecutor()
  def testCalc(i: Int)               = i + 1
  def testCalcString(i: Int): String = (i + 10).toString

  test("calculates"):
    val calc = Calculation.newCalculation(testCalc).build
    calc.run(1) should be(2)

  test("calls the ui updater with the calculated value"):
    val c    = new AtomicInteger(-1)
    val b    = new AtomicBoolean(false)
    val calc = Calculation.newCalculation(testCalc).whenResultsNotReady(b.set(true)).whenResultsReady(i => c.set(i)).build
    calc.run(1)
    b.get() should be(true)
    eventually:
      c.get() should be(2)

  test("notifies"):
    val c     = new AtomicInteger(-1)
    val calc2 = Calculation.newCalculation(testCalcString).whenResultsReady(i => c.set(i.toInt)).build
    val calc1 = Calculation.newCalculation(testCalc).notifyAfterCalculated(calc2).build
    calc1.run(1)
    eventually:
      c.get() should be(12)
