package org.terminal21.sparklib.calculations

import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}

import scala.annotation.implicitNotFound

@implicitNotFound("Unable to find ReadWriter for type ${A}. Dataset of case classes and Dataframes are supported.")
trait ReadWriter[A]:
  def read(spark: SparkSession, file: String): A
  def write(file: String, ds: A): Unit

object ReadWriter:
  given datasetReadWriter[A](using Encoder[A]): ReadWriter[Dataset[A]] = new ReadWriter[Dataset[A]]:
    override def read(spark: SparkSession, file: String)   = spark.read.parquet(file).as[A]
    override def write(file: String, ds: Dataset[A]): Unit = ds.write.parquet(file)

  given dataframeReadWriter: ReadWriter[DataFrame] = new ReadWriter[DataFrame]:
    override def read(spark: SparkSession, file: String)  = spark.read.parquet(file)
    override def write(file: String, ds: DataFrame): Unit = ds.write.parquet(file)
