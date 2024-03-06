package org.terminal21.sparklib

import functions.fibers.FiberExecutor
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession
import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.sparklib.calculations.SparkCalculation.TriggerRedraw
import org.terminal21.sparklib.calculations.{ReadWriter, SparkCalculation}
import org.terminal21.sparklib.util.Environment

import java.io.File

class Cached[OUT: ReadWriter](val name: String, outF: => OUT)(using spark: SparkSession):
  private val rw         = implicitly[ReadWriter[OUT]]
  private val rootFolder = s"${Environment.tmpDirectory}/spark-calculations"
  private val targetDir  = s"$rootFolder/$name"

  def isCached: Boolean = new File(targetDir).exists()

  def cachePath: String = targetDir

  private def cache[A](reader: => A, writer: => A): A =
    if isCached then reader
    else writer

  def invalidateCache(): Unit =
    FileUtils.deleteDirectory(new File(targetDir))
    out = None

  private def calculateOnce: OUT =
    cache(
      rw.read(spark, targetDir), {
        val ds = outF
        rw.write(targetDir, ds)
        ds
      }
    )

  @volatile private var out                              = Option.empty[OUT]
  private def startCalc(session: ConnectedSession): Unit =
    fiberExecutor.submit:
      out = Some(calculateOnce)
      session.fireEvent(TriggerRedraw)

  def get: Option[OUT] = out

  def visualize(dataUi: UiElement & HasStyle)(
      toUi: OUT => UiElement & HasStyle
  )(using
      SparkSession
  )(using session: ConnectedSession, events: Events) =
    val sc = new SparkCalculation[OUT](s"spark-calc-$name", dataUi, toUi, this)

    if events.isClicked(sc.recalc) then
      invalidateCache()
      startCalc(session)
    else if events.isInitialRender then startCalc(session)
    sc

object Cached:
  def apply[OUT: ReadWriter](name: String)(outF: => OUT)(using spark: SparkSession): Cached[OUT] = new Cached(name, outF)
