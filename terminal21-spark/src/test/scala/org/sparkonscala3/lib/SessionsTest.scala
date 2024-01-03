package org.sparkonscala3.lib

import org.sparkonscala3.lib.testmodel.Person

import scala.util.Using

class SessionsTest extends AbstractSparkSuite:
  val people = for (i <- 1 to 10) yield Person(i.toString, s"text for row $i")

  test("creates/destroys session"):
    Using.resource(Sessions.newSparkSession()): spark =>
      ()

  test("Can convert to Dataframe"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val df = spark.sparkContext.parallelize(people, 16).toDF()
      df.as[Person].collect() should be(people.toArray)

  test("Can convert to Dataset"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val ds = spark.sparkContext.parallelize(people, 16).toDS()
      ds.collect() should be(people.toArray)

  test("Can write parquet"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val ds  = spark.sparkContext.parallelize(people, 16).toDS()
      val f   = randomTmpFilename
      ds.write.parquet(f)
      val rds = spark.read.parquet(f).as[Person]
      rds.collect() should be(rds.collect())

  test("Can write csv"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val ds  = spark.sparkContext.parallelize(people, 16).toDS()
      val f   = randomTmpFilename
      ds.write.option("header", true).csv(f)
      val rds = spark.read.option("header", true).csv(f).as[Person]
      rds.collect() should be(rds.collect())

  test("Can write json"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val ds  = spark.sparkContext.parallelize(people, 16).toDS()
      val f   = randomTmpFilename
      ds.write.json(f)
      val rds = spark.read.json(f).as[Person]
      rds.collect() should be(rds.collect())

  test("Can write orc"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val ds  = spark.sparkContext.parallelize(people, 16).toDS()
      val f   = randomTmpFilename
      ds.write.orc(f)
      val rds = spark.read.orc(f).as[Person]
      rds.collect() should be(rds.collect())

  test("Can mount as tmp table"):
    Using.resource(Sessions.newSparkSession()): spark =>
      import scala3encoders.given
      import spark.implicits.*
      val ds = spark.sparkContext.parallelize(people, 16).toDS()
      ds.createOrReplaceTempView("people")
      spark.sql("select * from people").count() should be(10)
