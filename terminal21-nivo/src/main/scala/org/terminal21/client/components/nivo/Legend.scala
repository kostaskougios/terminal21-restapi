package org.terminal21.client.components.nivo

case class Legend(
    anchor: String = "bottom-right",
    direction: String = "column",
    justify: Boolean = false,
    translateX: Int = 100,
    translateY: Int = 0,
    itemsSpacing: Int = 0,
    itemDirection: String = "left-to-right",
    itemWidth: Int = 80,
    itemHeight: Int = 20,
    itemOpacity: Float = 0.75,
    symbolSize: Int = 12,
    symbolShape: String = "circle",
    symbolBorderColor: String = "rgba(0, 0, 0, .5)",
    effects: Seq[Effect] = Seq(Effect())
)
