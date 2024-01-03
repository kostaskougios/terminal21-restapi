package org.sparkonscala3.lib

import org.sparkonscala3.lib.testmodel.Person

import scala.util.Using

class SparkSessionExtTest extends AbstractSparkSuite:
  val people = for (i <- 1 to 10) yield Person(i.toString, s"text for row $i")

  test("schemaOf"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      val sp     = new SparkSessionExt(spark)
      import scala3encoders.given
      import spark.implicits.*
      val schema = sp.schemaOf[Person]
      schema.toList.size should be(2)

  test("toDF"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      val sp = new SparkSessionExt(spark)
      import scala3encoders.given
      import spark.implicits.*
      val df = sp.toDF(people)
      df.as[Person].collect() should be(people.toArray)

  test("toDS"):
    Using.resource(SparkSessions.newSparkSession()): spark =>
      val sp = new SparkSessionExt(spark)
      import scala3encoders.given
      import spark.implicits.*
      val ds = sp.toDS(people)
      ds.collect() should be(people.toArray)
