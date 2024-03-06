package org.terminal21.sparklib.calculations

import functions.fibers.{Fiber, FiberExecutor}
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession
import org.terminal21.client.{*, given}
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.*
import org.terminal21.collections.TypedMap
import org.terminal21.model.ClientEvent
import org.terminal21.sparklib.Cached
import org.terminal21.sparklib.calculations.SparkCalculation.TriggerRedraw
import org.terminal21.sparklib.util.Environment

import java.io.File

/** A UI component that takes a spark calculation (i.e. a spark query) that results in a Dataset. It caches the results by storing them as parquet into the tmp
  * folder/spark-calculations/$name. Next time the calculation runs it reads the cache if available. A button should allow the user to clear the cache and rerun
  * the spark calculations in case the data changed.
  *
  * Because the cache is stored in the disk, it is available even if the jvm running the code restarts. This allows the user to run and rerun their code without
  * having to rerun the spark calculation.
  */
case class SparkCalculation[OUT: ReadWriter](
    key: String,
    dataUi: UiElement with HasStyle,
    toUi: OUT => UiElement & HasStyle,
    cached: Cached[OUT],
    dataStore: TypedMap = TypedMap.Empty
)(using
    spark: SparkSession,
    session: ConnectedSession,
    events: Events
) extends UiComponent:
  def name = cached.name
  override type This = SparkCalculation[OUT]
  override def withKey(key: String): This        = copy(key = key)
  override def withDataStore(ds: TypedMap): This = copy(dataStore = ds)

  val recalc = Button(s"recalc-button-$name", text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon()))

  override def rendered: Seq[UiElement] =
    val header = Box(
      s"recalc-box-$name",
      bg = "green",
      p = 4,
      children = Seq(
        HStack().withChildren(
          Text(text = name),
          if events.isClicked(recalc) then Badge(text = "Recalculating...")
          else if events.isInitialRender then Badge(text = "Initializing...")
          else recalc
        )
      )
    )
    val ui     = cached.get
      .map: ds =>
        toUi(ds)
      .getOrElse(dataUi)

    Seq(header, ui)

object SparkCalculation:
  object TriggerRedraw extends ClientEvent
