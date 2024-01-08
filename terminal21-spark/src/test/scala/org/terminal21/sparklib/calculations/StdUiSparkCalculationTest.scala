package org.terminal21.sparklib.calculations

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.time.{Millis, Span}
import org.terminal21.client.components.Keys
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock, given}
import org.terminal21.sparklib.SparkSessions

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}
import scala.util.Using

class StdUiSparkCalculationTest extends AnyFunSuiteLike with Eventually:
  given PatienceConfig = PatienceConfig(scaled(Span(3000, Millis)))

  test("calculates the correct result"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val calc               = new TestingCalculation
      calc.run().get().collect().toList should be(List(2))

  test("whenResultsNotReady"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val called             = new AtomicBoolean(false)
      val calc               = new TestingCalculation:
        override protected def whenResultsNotReady(): Unit =
          called.set(true)
      calc.run().get()
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

      calc.run()
      eventually:
        called.get() should be(true)

  test("whenResultsReady called even when cached"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val called             = new AtomicInteger(0)
      val calc               = new TestingCalculation:
        override protected def whenResultsReady(results: Dataset[Int]): Unit =
          results.collect().toList should be(List(2))
          called.incrementAndGet()

      calc.run().get()
      calc.run()
      eventually:
        called.get() should be(2)

  test("caches results"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val calc               = new TestingCalculation
      calc.run().get().collect().toList should be(List(2))
      calc.run().get().collect().toList should be(List(2))
      calc.calcCalledTimes.get() should be(1)

  test("refresh button invalidates cache and runs calculations"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession              = spark
      val calc                        = new TestingCalculation
      calc.run().get().collect().toList should be(List(2))
      session.click(calc.recalc)
      eventually:
        calc.calcCalledTimes.get() should be(2)

class TestingCalculation(using session: ConnectedSession, spark: SparkSession, intEncoder: Encoder[Int])
    extends StdUiSparkCalculation[Int](Keys.nextKey, "testing-calc", Box()):
  val calcCalledTimes = new AtomicInteger(0)
  invalidateCache()

  override def nonCachedCalculation: Dataset[Int] =
    import spark.implicits.*
    calcCalledTimes.incrementAndGet()
    Seq(2).toDS
