package org.terminal21.sparklib.steps

import functions.fibers.FiberExecutor
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.{Calculation, ConnectedSession}
import org.terminal21.client.components.chakra.{Badge, Box, Button, HStack, RepeatIcon, Text}
import org.terminal21.client.components.{Keys, UiComponent, UiElement}
import org.terminal21.sparklib.util.Environment

import java.io.File

abstract class SparkCalculation[IN, OUT](
    val key: String = Keys.nextKey,
    @volatile var children: Seq[UiElement],
    notifyWhenCalcReady: Seq[Calculation[Dataset[OUT], _]]
)(using executor: FiberExecutor)
    extends Calculation[IN, Dataset[OUT]](notifyWhenCalcReady)
    with UiComponent

abstract class StdSparkCalculation[IN, OUT: Encoder](
    name: String,
    dataUi: UiElement with HasStyle,
    notifyWhenCalcReady: Seq[Calculation[Dataset[OUT], _]],
    key: String = Keys.nextKey
)(using session: ConnectedSession, executor: FiberExecutor, spark: SparkSession)
    extends SparkCalculation[IN, OUT](key, Nil, notifyWhenCalcReady):
  val badge  = Badge()
  val recalc = Button(text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon())).onClick: () =>
    badge.text = "Invalidating cache ..."
    session.render()
    invalidateCache()
    for i <- in do run(i)

  children = Seq(
    Box(bg = "green", p = 4).withChildren(
      HStack().withChildren(
        Text(text = name),
        badge,
        recalc
      )
    ),
    dataUi
  )

  private val rootFolder                              = s"${Environment.tmpDirectory}/spark-calculations/$name"
  private val targetDir                               = s"$rootFolder/$name"
  private def cache[A](reader: => A, writer: => A): A =
    if new File(targetDir).exists() then reader
    else writer

  def invalidateCache(): Unit =
    FileUtils.deleteDirectory(new File(targetDir))

  private def calculateOnce(f: => Dataset[OUT]): Dataset[OUT] =
    cache(
      spark.read.parquet(targetDir).as[OUT], {
        val ds = f
        ds.write.parquet(targetDir)
        ds
      }
    )

  private var in: Option[IN] = None
  override def run(in: IN)   =
    this.in = Some(in)
    calculateOnce(super.run(in))

  override protected def whenResultsNotReady(): Unit =
    badge.text = "Calculating"
    badge.colorScheme = Some("purple")
    recalc.isDisabled = Some(true)
    dataUi.style = dataUi.style + ("filter" -> "grayscale(100%)")
    session.render()
    super.whenResultsNotReady()

  override protected def whenResultsReady(results: Dataset[OUT]): Unit =
    badge.text = "Ready"
    badge.colorScheme = None
    recalc.isDisabled = Some(false)
    dataUi.style = dataUi.style - "filter"
    session.render()

object SparkCalculation:
  class Builder[IN, OUT: Encoder](
      name: String,
      dataUi: UiElement with HasStyle,
      notifyWhenCalcReady: Seq[Calculation[Dataset[OUT], _]],
      calc: IN => Dataset[OUT]
  )(using
      session: ConnectedSession,
      executor: FiberExecutor,
      spark: SparkSession
  ):
    def whenResultsReady(ready: Dataset[OUT] => Unit) =
      new StdSparkCalculation[IN, OUT](name, dataUi, notifyWhenCalcReady):
        override protected def calculation(in: IN): Dataset[OUT]             = calc(in)
        override protected def whenResultsReady(results: Dataset[OUT]): Unit =
          ready(results)
          super.whenResultsReady(results)

  def sparkCalculation[IN, OUT: Encoder](name: String, dataUi: UiElement with HasStyle, notifyWhenCalcReady: Calculation[Dataset[OUT], _]*)(
      calc: IN => Dataset[OUT]
  )(using
      session: ConnectedSession,
      executor: FiberExecutor,
      spark: SparkSession
  ) =
    new Builder[IN, OUT](name, dataUi, notifyWhenCalcReady, calc)
