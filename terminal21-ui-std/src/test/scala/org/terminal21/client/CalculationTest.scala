package org.terminal21.client

import functions.fibers.FiberExecutor
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import org.scalatest.concurrent.Eventually.*
import org.terminal21.client.components.Calculation

class CalculationTest extends AnyFunSuiteLike:
  given executor: FiberExecutor      = FiberExecutor()
  def testCalc(i: Int)               = i + 1
  def testCalcString(i: Int): String = (i + 10).toString

  class Calc extends Calculation[Int]:
    val whenResultsNotReadyCalled                               = new AtomicBoolean(false)
    val whenResultsReadyValue                                   = new AtomicInteger(-1)
    override protected def whenResultsNotReady(): Unit          = whenResultsNotReadyCalled.set(true)
    override protected def whenResultsReady(results: Int): Unit = whenResultsReadyValue.set(results)
    override protected def calculation()                        = 2

  test("calculates"):
    val calc = new Calc
    calc.run().get() should be(2)

  test("calls whenResultsNotReady"):
    val calc = new Calc
    calc.run()
    eventually:
      calc.whenResultsNotReadyCalled.get() should be(true)

  test("calls whenResultsReady"):
    val calc = new Calc
    calc.run()
    eventually:
      calc.whenResultsReadyValue.get() should be(2)
