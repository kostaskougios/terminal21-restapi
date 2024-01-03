package org.terminal21.sparklib

import org.apache.spark.sql.SparkSession
import org.terminal21.client.{ConnectedSession, Sessions}

import scala.util.Using

object SparkSessions:
  def newSparkSession(
      appName: String = "spark-app",
      master: String = "local[*]",
      bindAddress: String = "localhost",
      sparkUiEnabled: Boolean = false
  ): SparkSession =
    SparkSession
      .builder()
      .appName(appName)
      .master(master)
      .config("spark.driver.bindAddress", bindAddress)
      .config("spark.ui.enabled", sparkUiEnabled)
      .getOrCreate()

  /** Will create a terminal21 session and use the provided spark session
    * @param spark
    *   the spark session, will be closed before this call returns. Use #newSparkSession to quickly create one.
    * @param id
    *   the id of the terminal21 session
    * @param name
    *   the name of the terminal21 session
    * @param f
    *   the code to run
    * @tparam R
    *   if f returns some value, this will be returned by the method
    * @return
    *   whatever f returns
    */
  def newTerminal21WithSparkSession[R](spark: SparkSession, id: String, name: String)(f: (SparkSession, ConnectedSession) => R): R =
    Sessions.withNewSession(id, name): terminal21Session =>
      Using.resource(spark): sp =>
        f(spark, terminal21Session)
