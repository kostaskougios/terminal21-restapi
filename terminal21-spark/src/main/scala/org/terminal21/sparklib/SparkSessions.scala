package org.terminal21.sparklib

import org.apache.spark.sql.SparkSession
import org.terminal21.client.components.ComponentLib
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
