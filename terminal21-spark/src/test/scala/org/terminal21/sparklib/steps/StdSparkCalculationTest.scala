package org.terminal21.sparklib.steps

import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock, given}

import java.util.concurrent.atomic.AtomicBoolean

class StdSparkCalculationTest extends AnyFunSuiteLike:
  test("whenResultsNotReady"):
    given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val dataUi             = Box()
    val called             = new AtomicBoolean(false)
    val calc               = new StdSparkCalculation[Int, Int]("key", "name", dataUi, Nil):
      override protected def calculation(in: Int) = in + 1

      override protected def whenResultsNotReady(): Unit =
        called.set(true)
        super.whenResultsNotReady()

    calc.run(1)
    called.get() should be(true)

  test("whenResultsReady"):
    given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val dataUi             = Box()
    val called             = new AtomicBoolean(false)
    val calc               = new StdSparkCalculation[Int, Int]("key", "name", dataUi, Nil):
      override protected def calculation(in: Int) = in + 1

      override protected def whenResultsReady(results: Int): Unit =
        called.set(true)
        super.whenResultsReady(results)

    calc.run(1)
    eventually:
      called.get() should be(true)
