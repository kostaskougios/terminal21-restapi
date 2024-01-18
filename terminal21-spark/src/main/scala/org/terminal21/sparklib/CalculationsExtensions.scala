package org.terminal21.sparklib

import functions.fibers.FiberExecutor
import org.apache.spark.sql.SparkSession
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.sparklib.calculations.{ReadWriter, StdUiSparkCalculation}

extension [OUT: ReadWriter](ds: OUT)
  def visualize(name: String, dataUi: UiElement with HasStyle[_])(
      toUi: OUT => UiElement
  )(using
      session: ConnectedSession,
      executor: FiberExecutor,
      spark: SparkSession
  ) =
    val ui = new StdUiSparkCalculation[OUT](Keys.nextKey, name, dataUi):
      override protected def whenResultsReady(results: OUT): Unit =
        try toUi(results).renderChanges()
        catch case t: Throwable => t.printStackTrace()
        super.whenResultsReady(results)
      override def nonCachedCalculation: OUT                      = ds

    ui.run()
    ui
