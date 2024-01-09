package org.terminal21.client.components.nivo

case class ResponsiveLineData(
    id: String,
    color: String = "hsl(88, 70%, 50%)",
    data: Seq[XYCoordinates] = Nil
)

case class XYCoordinates(
    x: String,
    y: Float
)
