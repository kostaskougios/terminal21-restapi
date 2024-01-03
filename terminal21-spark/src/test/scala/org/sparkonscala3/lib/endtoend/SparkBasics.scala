package org.sparkonscala3.lib.endtoend

import org.sparkonscala3.lib.SparkSessions

import scala.util.Using

@main def sparkBasics() =
  Using.resource(SparkSessions.newSparkSession()): spark =>
    ()
