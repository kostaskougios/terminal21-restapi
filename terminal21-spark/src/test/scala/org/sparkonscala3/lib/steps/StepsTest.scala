package org.sparkonscala3.lib.steps

import org.sparkonscala3.lib.{AbstractSparkSuite, Sessions, SparkSessionExt}

import java.util.concurrent.atomic.AtomicInteger
import scala.util.Using

class StepsTest extends AbstractSparkSuite:
  test("returns the correct dataframe"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val steps      = Steps(spark, "StepsTest-correct-df")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      def calculations() = Seq(1, 2, 3).toDF()

      for _ <- 1 to 3 do budgetStep.calculateOnce(calculations()).as[Int].collect().sorted should be(Array(1, 2, 3))

  test("returns the correct dataset"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val steps      = Steps(spark, "StepsTest-correct-ds")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      def calculations() = Seq(1, 2, 3).toDS()

      for _ <- 1 to 3 do budgetStep.calculateOnce(calculations()).as[Int].collect().sorted should be(Array(1, 2, 3))

  test("doesn't re-evaluate cached dataframes"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val steps      = Steps(spark, "StepsTest-cachedDF")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      val calculated = new AtomicInteger(0)

      def calculations() =
        calculated.incrementAndGet()
        Seq(1, 2, 3).toDF()

      budgetStep.calculateOnce(calculations())
      budgetStep.calculateOnce(calculations())
      calculated.get() should be(1)

  test("doesn't re-evaluate cached datasets"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import spark.implicits.*

      val steps      = Steps(spark, "StepsTest-cachedDS")
      val budgetStep = steps.step("budget")
      budgetStep.invalidateCache()

      val calculated = new AtomicInteger(0)

      def calculations() =
        calculated.incrementAndGet()
        Seq(1, 2, 3).toDS()
      budgetStep.calculateOnce(calculations())
      budgetStep.calculateOnce(calculations())
      calculated.get() should be(1)
