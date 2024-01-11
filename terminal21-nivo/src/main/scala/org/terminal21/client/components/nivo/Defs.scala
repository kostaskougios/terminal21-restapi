package org.terminal21.client.components.nivo

case class Defs(
    id: String = "dots",
    `type`: String = "patternDots",
    background: String = "inherit",
    color: String = "#38bcb2",
    size: Option[Int] = None,
    padding: Option[Float] = None,
    stagger: Option[Boolean] = None,
    rotation: Option[Int] = None,
    lineWidth: Option[Int] = None,
    spacing: Option[Int] = None
)
