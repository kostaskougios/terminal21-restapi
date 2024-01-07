package org.terminal21.sparklib.steps

import functions.fibers.FiberExecutor
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.{CachedCalculation, Calculation, ConnectedSession}
import org.terminal21.client.components.chakra.{Badge, Box, Button, HStack, RepeatIcon, Text}
import org.terminal21.client.components.{Keys, UiComponent, UiElement}
import org.terminal21.sparklib.util.Environment

import java.io.File

/** A UI component that takes a spark calculation (i.e. a spark query) that results in a Dataset. It caches the results by storing them as parquet into the tmp
  * folder/spark-calculations/$name. Next time the calculation runs it reads the cache if available. A button should allow the user to clear the cache and rerun
  * the spark calculations in case the data changed.
  *
  * Because the cache is stored in the disk, it is available even if the jvm running the code restarts. This allows the user to run and rerun their code without
  * having to rerun the spark calculation.
  *
  * Subclass this to create your own UI for a spark calculation, see StdUiSparkCalculation below.
  */
abstract class SparkCalculation[IN, OUT: Encoder](
    val key: String = Keys.nextKey,
    name: String,
    @volatile var children: Seq[UiElement],
    notifyWhenCalcReady: Seq[Calculation[Dataset[OUT], _]]
)(using executor: FiberExecutor, spark: SparkSession)
    extends CachedCalculation[IN, Dataset[OUT]](notifyWhenCalcReady)
    with UiComponent:
  private val rootFolder = s"${Environment.tmpDirectory}/spark-calculations/$name"
  private val targetDir  = s"$rootFolder/$name"

  def isCached: Boolean = new File(targetDir).exists()

  private def cache[A](reader: => A, writer: => A): A =
    if isCached then reader
    else writer

  override def invalidateCache(): Unit =
    FileUtils.deleteDirectory(new File(targetDir))
    super.invalidateCache()

  private def calculateOnce(f: => Dataset[OUT]): Dataset[OUT] =
    cache(
      spark.read.parquet(targetDir).as[OUT], {
        val ds = f
        ds.write.parquet(targetDir)
        ds
      }
    )

  protected var in: Option[IN] = None

  override def run(in: IN) =
    this.in = Some(in)
    val isC = isCached
    val out = calculateOnce(super.run(in))
    if isC then postRun(out)
    out

abstract class StdUiSparkCalculation[IN, OUT: Encoder](
    name: String,
    dataUi: UiElement with HasStyle,
    notifyWhenCalcReady: Seq[Calculation[Dataset[OUT], _]],
    key: String = Keys.nextKey
)(using session: ConnectedSession, executor: FiberExecutor, spark: SparkSession)
    extends SparkCalculation[IN, OUT](name, key, Nil, notifyWhenCalcReady):
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
      new StdUiSparkCalculation[IN, OUT](name, dataUi, notifyWhenCalcReady):
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
