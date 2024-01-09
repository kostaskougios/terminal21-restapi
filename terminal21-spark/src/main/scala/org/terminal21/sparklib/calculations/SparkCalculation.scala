package org.terminal21.sparklib.calculations

import functions.fibers.FiberExecutor
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.{CachedCalculation, UiComponent, UiElement}
import org.terminal21.client.ConnectedSession
import org.terminal21.sparklib.util.Environment

import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/** A UI component that takes a spark calculation (i.e. a spark query) that results in a Dataset. It caches the results by storing them as parquet into the tmp
  * folder/spark-calculations/$name. Next time the calculation runs it reads the cache if available. A button should allow the user to clear the cache and rerun
  * the spark calculations in case the data changed.
  *
  * Because the cache is stored in the disk, it is available even if the jvm running the code restarts. This allows the user to run and rerun their code without
  * having to rerun the spark calculation.
  *
  * Subclass this to create your own UI for a spark calculation, see StdUiSparkCalculation below.
  */
abstract class SparkCalculation[OUT: ReadWriter](
    val key: String,
    name: String,
    @volatile var children: Seq[UiElement]
)(using executor: FiberExecutor, spark: SparkSession)
    extends CachedCalculation[OUT]
    with UiComponent:
  private val rw         = implicitly[ReadWriter[OUT]]
  private val rootFolder = s"${Environment.tmpDirectory}/spark-calculations"
  private val targetDir  = s"$rootFolder/$name"

  def isCached: Boolean = new File(targetDir).exists()

  private def cache[A](reader: => A, writer: => A): A =
    if isCached then reader
    else writer

  override def invalidateCache(): Unit =
    FileUtils.deleteDirectory(new File(targetDir))

  private def calculateOnce(f: => OUT): OUT =
    cache(
      rw.read(spark, targetDir), {
        val ds = f
        rw.write(targetDir, ds)
        ds
      }
    )

  override protected def calculation(): OUT = calculateOnce(nonCachedCalculation)

abstract class StdUiSparkCalculation[OUT: ReadWriter](
    key: String,
    name: String,
    dataUi: UiElement with HasStyle
)(using session: ConnectedSession, executor: FiberExecutor, spark: SparkSession)
    extends SparkCalculation[OUT](key, name, Nil):
  val badge           = Badge()
  private val running = new AtomicBoolean(false)
  val recalc          = Button(text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon())).onClick: () =>
    if running.compareAndSet(false, true) then
      try
        badge.text = "Invalidating cache ..."
        session.render()
        invalidateCache()
        run()
      finally running.set(false)

  val header = Box(bg = "green", p = 4).withChildren(
    HStack().withChildren(
      Text(text = name),
      badge,
      recalc
    )
  )
  children = Seq(
    header,
    dataUi
  )

  override protected def whenResultsNotReady(): Unit =
    badge.text = "Calculating"
    badge.colorScheme = Some("purple")
    recalc.isDisabled = Some(true)
    dataUi.style = dataUi.style + ("filter" -> "grayscale(100%)")
    session.render()
    super.whenResultsNotReady()

  override protected def whenResultsReady(results: OUT): Unit =
    badge.text = "Ready"
    badge.colorScheme = None
    recalc.isDisabled = Some(false)
    dataUi.style = dataUi.style - "filter"
    session.render()
