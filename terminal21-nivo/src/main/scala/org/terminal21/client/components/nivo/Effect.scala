package org.terminal21.client.components.nivo

case class Effect(
    on: String = "hover",
    style: Map[String, Any] = Map(
      "itemBackground" -> "rgba(0, 0, 0, .03)",
      "itemOpacity"    -> 1
    )
)
