package org.sparkonscala3.lib.endtoend

import org.terminal21.sparklib.SparkSessions
import org.terminal21.sparklib.steps.Steps

import scala.util.Using

@main def sparkBasics() =
  Using.resource(SparkSessions.newSparkSession()): spark =>
    val steps = Steps(spark, "spark-basics")
