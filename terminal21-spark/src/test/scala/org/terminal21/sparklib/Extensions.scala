package org.terminal21.sparklib

import functions.fibers.FiberExecutor
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.sparklib.calculations.StdUiSparkCalculation

extension [OUT: Encoder](ds: Dataset[OUT])
  def visualize(name: String, dataUi: UiElement with HasStyle)(
      toUi: Dataset[OUT] => Unit
  )(using
      session: ConnectedSession,
      executor: FiberExecutor,
      spark: SparkSession
  ) =
    val ui = new StdUiSparkCalculation[OUT](Keys.nextKey, name, dataUi):
      override protected def whenResultsReady(results: Dataset[OUT]): Unit =
        toUi(results)
        super.whenResultsReady(results)
      override def nonCachedCalculation: Dataset[OUT]                      = ds

    ui.run()
    ui
