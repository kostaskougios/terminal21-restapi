package org.terminal21.sparklib.calculations

import functions.fibers.FiberExecutor
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession
import org.terminal21.client.{ConnectedSession, Model}
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{CachedCalculation, StdUiCalculation, UiComponent, UiElement}
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
trait SparkCalculation[OUT: ReadWriter](name: String)(using executor: FiberExecutor, spark: SparkSession) extends CachedCalculation[OUT] with UiComponent:
  private val rw         = implicitly[ReadWriter[OUT]]
  private val rootFolder = s"${Environment.tmpDirectory}/spark-calculations"
  private val targetDir  = s"$rootFolder/$name"

  def isCached: Boolean = new File(targetDir).exists()
  def cachePath: String = targetDir

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
    val key: String,
    name: String,
    dataUi: UiElement with HasStyle
)(using ConnectedSession, Model[_], FiberExecutor, SparkSession)
    extends SparkCalculation[OUT](name)
    with StdUiCalculation[OUT](name, dataUi)
