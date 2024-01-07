package org.terminal21.sparklib.steps

import functions.fibers.FiberExecutor
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock, given}
import org.terminal21.sparklib.SparkSessions

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import scala.util.Using

class StdSparkCalculationTest extends AnyFunSuiteLike:
  test("calculates the correct result"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val calc               = new TestingCalculation
      calc.run(1).collect().toList should be(List(2))

  test("whenResultsNotReady"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val called             = new AtomicBoolean(false)
      val calc               = new TestingCalculation:
        override protected def whenResultsNotReady(): Unit =
          called.set(true)
      calc.run(1)
      called.get() should be(true)

  test("whenResultsReady"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val called             = new AtomicBoolean(false)
      val calc               = new TestingCalculation:
        override protected def whenResultsReady(results: Dataset[Int]): Unit =
          results.collect().toList should be(List(2))
          called.set(true)

      calc.run(1)
      eventually:
        called.get() should be(true)

  test("caches results"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark

      val calc = new TestingCalculation

      calc.run(1).collect().toList should be(List(2))
      calc.run(1).collect().toList should be(List(2))
      calc.calcCalledTimes.get() should be(1)

class TestingCalculation(using session: ConnectedSession, executor: FiberExecutor, spark: SparkSession, intEncoder: Encoder[Int])
    extends StdSparkCalculation[Int, Int]("testing-calc", Box(), Nil):
  val calcCalledTimes                         = new AtomicInteger(0)
  invalidateCache()
  override protected def calculation(in: Int) =
    import spark.implicits.*
    calcCalledTimes.incrementAndGet()
    Seq(in + 1).toDS
