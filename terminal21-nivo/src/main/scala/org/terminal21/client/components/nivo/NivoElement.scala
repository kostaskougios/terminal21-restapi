package org.terminal21.client.components.nivo

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.collections.TypedMap

sealed trait NEJson      extends UiElement
sealed trait NivoElement extends NEJson with HasStyle

/** https://nivo.rocks/line/
  */
case class ResponsiveLine(
    key: String = Keys.nextKey,
    // to give width and height, we wrap the component in a wrapper element. Height must be provided
    // for nivo components to be visible
    style: Map[String, Any] = Map("height" -> "400px"),
    data: Seq[Serie] = Nil,
    margin: Margin = Margin(right = 110),
    xScale: Scale = Scale.Point,
    yScale: Scale = Scale(),
    yFormat: String = " >-.2f",
    axisTop: Option[Axis] = None,
    axisRight: Option[Axis] = None,
    axisBottom: Option[Axis] = Some(Axis(legend = "y", legendOffset = 36)),
    axisLeft: Option[Axis] = Some(Axis(legend = "x", legendOffset = -40)),
    pointSize: Int = 10,
    pointColor: Map[String, String] = Map("theme" -> "background"),
    pointBorderWidth: Int = 2,
    pointBorderColor: Map[String, String] = Map("from" -> "serieColor"),
    pointLabelYOffset: Int = -12,
    useMesh: Boolean = true,
    legends: Seq[Legend] = Nil,
    dataStore: TypedMap = TypedMap.empty
) extends NivoElement:
  type This = ResponsiveLine
  override def withStyle(v: Map[String, Any]): ResponsiveLine = copy(style = v)
  def withKey(v: String)                                      = copy(key = v)
  def withData(data: Seq[Serie])                              = copy(data = data)
  override def withDataStore(ds: TypedMap)                    = copy(dataStore = ds)

/** https://nivo.rocks/bar/
  */
case class ResponsiveBar(
    key: String = Keys.nextKey,
    // to give width and height, we wrap the component in a wrapper element. Height must be provided
    // for nivo components to be visible
    style: Map[String, Any] = Map("height" -> "400px"),
    data: Seq[Seq[BarDatum]] = Nil,
    keys: Seq[String] = Nil,
    indexBy: String = "",
    margin: Margin = Margin(right = 110),
    padding: Float = 0,
    valueScale: Scale = Scale(),
    indexScale: Scale = Scale(),
    colors: Map[String, String] = Map("scheme" -> "nivo"),
    defs: Seq[Defs] = Nil,
    fill: Seq[Fill] = Nil,
    axisTop: Option[Axis] = None,
    axisRight: Option[Axis] = None,
    axisBottom: Option[Axis] = Some(Axis(legend = "y", legendOffset = 36)),
    axisLeft: Option[Axis] = Some(Axis(legend = "x", legendOffset = -40)),
    legends: Seq[Legend] = Nil,
    ariaLabel: String = "Chart Label",
    dataStore: TypedMap = TypedMap.empty
) extends NivoElement:
  type This = ResponsiveBar
  override def withStyle(v: Map[String, Any]): ResponsiveBar = copy(style = v)
  def withKey(v: String)                                     = copy(key = v)
  def withData(data: Seq[Seq[BarDatum]])                     = copy(data = data)
  override def withDataStore(ds: TypedMap)                   = copy(dataStore = ds)
