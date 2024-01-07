package org.terminal21.sparklib.steps

import org.apache.spark.sql.{Dataset, SparkSession}
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

      val calc = new StdSparkCalculation[Int, Int]("name", Box(), Nil):
        override protected def calculation(in: Int) = Seq(in + 1).toDS

      calc.invalidateCache()
      calc.run(1).collect().toList should be(List(2))

  test("whenResultsNotReady"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val called             = new AtomicBoolean(false)
      val calc               = new StdSparkCalculation[Int, Int]("name", Box(), Nil):
        override protected def calculation(in: Int)        = Seq(in + 1).toDS
        override protected def whenResultsNotReady(): Unit =
          called.set(true)
      calc.invalidateCache()
      calc.run(1)
      called.get() should be(true)

  test("whenResultsReady"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark
      val called             = new AtomicBoolean(false)
      val calc               = new StdSparkCalculation[Int, Int]("name", Box(), Nil):
        override protected def calculation(in: Int)                          = Seq(in + 1).toDS()
        override protected def whenResultsReady(results: Dataset[Int]): Unit =
          results.collect().toList should be(List(2))
          called.set(true)

      calc.invalidateCache()
      calc.run(1)
      eventually:
        called.get() should be(true)

  test("caches results"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      import spark.implicits.*
      given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
      given SparkSession     = spark

      val called = new AtomicInteger(0)
      val calc   = new StdSparkCalculation[Int, Int]("name", Box(), Nil):
        override protected def calculation(in: Int) =
          called.incrementAndGet()
          Seq(in + 1).toDS

      calc.invalidateCache()
      calc.run(1).collect().toList should be(List(2))
      calc.run(1).collect().toList should be(List(2))
      called.get() should be(1)
