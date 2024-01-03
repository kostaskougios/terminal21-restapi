package org.terminal21.sparklib.steps

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}
import org.terminal21.sparklib.util.Environment

import java.io.File

class Steps(spark: SparkSession, name: String):
  private val rootFolder = s"${Environment.tmpDirectory}/steps/$name"

  def step(name: String): Step = new Step(spark, rootFolder, name)

class Step(spark: SparkSession, rootFolder: String, name: String):
  val targetDir = s"$rootFolder/$name"

  def invalidateCache(): Unit =
    FileUtils.deleteDirectory(new File(targetDir))

  private def cache[A](reader: => A, writer: => A): A =
    if new File(targetDir).exists() then reader
    else writer

  def calculateOnce(f: => DataFrame): DataFrame =
    cache(
      spark.read.parquet(targetDir), {
        val df = f
        df.write.parquet(targetDir)
        df
      }
    )

  def calculateOnce[A: Encoder](f: => Dataset[A]): Dataset[A] =
    cache(
      spark.read.parquet(targetDir).as[A], {
        val ds = f
        ds.write.parquet(targetDir)
        ds
      }
    )
