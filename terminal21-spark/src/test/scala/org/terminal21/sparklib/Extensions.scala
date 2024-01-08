package org.terminal21.sparklib

import functions.fibers.FiberExecutor
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.terminal21.client.{Calculation, ConnectedSession}
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.sparklib.calculations.StdUiSparkCalculation

extension [OUT: Encoder](ds: Dataset[OUT])
  def visualize(name: String, dataUi: UiElement with HasStyle, notifyWhenReady: Calculation[Dataset[OUT], _]*)(
      toUi: Dataset[OUT] => Unit
  )(using
      session: ConnectedSession,
      executor: FiberExecutor,
      spark: SparkSession
  ) =
    val ui = new StdUiSparkCalculation[Unit, OUT](name, dataUi, notifyWhenReady):
      override protected def whenResultsReady(results: Dataset[OUT]): Unit =
        toUi(ds)
        super.whenResultsReady(results)

      override protected def calculation(in: Unit): Dataset[OUT] = ds

    executor.submit:
      ui.run(())
    ui
