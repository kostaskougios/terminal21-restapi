package org.terminal21.sparklib

import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}

import scala.reflect.ClassTag

class SparkSessionExt(spark: SparkSession):
  import spark.implicits.*

  def schemaOf[P: Encoder] = summon[Encoder[P]].schema

  def toDF[P: ClassTag: Encoder](s: Seq[P], numSlices: Int = spark.sparkContext.defaultParallelism): DataFrame =
    spark.sparkContext.parallelize(s, numSlices).toDF()

  def toDS[P: ClassTag: Encoder](s: Seq[P], numSlices: Int = spark.sparkContext.defaultParallelism): Dataset[P] =
    spark.sparkContext.parallelize(s, numSlices).toDS()
