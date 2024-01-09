package org.terminal21.client.components.nivo

import org.terminal21.client.components.{Keys, UiElement}

sealed trait NivoElement extends UiElement

/** https://nivo.rocks/line/
  */
case class ResponsiveLine(
    key: String = Keys.nextKey,
    // to give width and height, we wrap the component in a wrapper element. Height must be provided
    // for nivo components to be visible
    @volatile var wrapperStyle: Map[String, String] = Map("height" -> "400px"),
    @volatile var data: Seq[Serie] = Nil,
    @volatile var margin: Margin = Margin(),
    @volatile var xScale: Scale = Scale.Point,
    @volatile var yScale: Scale = Scale(),
    @volatile var yFormat: String = " >-.2f",
    @volatile var axisTop: Option[Axis] = None,
    @volatile var axisRight: Option[Axis] = None,
    @volatile var axisBottom: Option[Axis] = Some(Axis(legend = "y", legendOffset = 36)),
    @volatile var axisLeft: Option[Axis] = Some(Axis(legend = "x", legendOffset = -40)),
    @volatile var pointSize: Int = 10,
    @volatile var pointBorderWidth: Int = 2,
    @volatile var pointLabelYOffset: Int = -12,
    @volatile var useMesh: Boolean = true
) extends NivoElement
