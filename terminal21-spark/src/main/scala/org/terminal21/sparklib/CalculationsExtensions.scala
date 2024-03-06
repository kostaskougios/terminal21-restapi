package org.terminal21.sparklib

import functions.fibers.FiberExecutor
import org.apache.spark.sql.SparkSession
import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.sparklib.calculations.{ReadWriter, SparkCalculation}

extension [OUT: ReadWriter](ds: OUT)
  def visualize(name: String, dataUi: UiElement & HasStyle)(
      toUi: OUT => UiElement & HasStyle
  )(using
      ConnectedSession,
      FiberExecutor,
      SparkSession,
      Events
  ) =
    val ui = new SparkCalculation[OUT](s"spark-calc-$name", name, dataUi, toUi, ds)
    ui.runCalculation()
    ui
