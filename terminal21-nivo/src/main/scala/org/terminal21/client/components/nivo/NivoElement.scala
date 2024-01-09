package org.terminal21.client.components.nivo

import org.terminal21.client.components.{Keys, UiElement}

sealed trait NivoElement extends UiElement

case class ResponsiveLine(
    key: String = Keys.nextKey,
    data: Seq[Serie] = Nil,
    margin: Margin = Margin(),
    xScale: Scale = Scale.Point,
    yScale: Scale = Scale(),
    yFormat: String = " >-.2f",
    axisTop: Option[Axis] = None,
    axisRight: Option[Axis] = None,
    axisBottom: Option[Axis] = Some(Axis(legend = "y", legendOffset = 36)),
    axisLeft: Option[Axis] = Some(Axis(legend = "x", legendOffset = -40)),
    pointSize: Int = 10,
    pointBorderWidth: Int = 2,
    pointLabelYOffset: Int = -12,
    useMesh: Boolean = true
) extends NivoElement
