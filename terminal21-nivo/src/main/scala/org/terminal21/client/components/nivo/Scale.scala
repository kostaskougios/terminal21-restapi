package org.terminal21.client.components.nivo

case class Scale(
    `type`: String = "linear",
    min: Option[String] = Some("auto"),
    max: Option[String] = Some("auto"),
    stacked: Option[Boolean] = Some(false),
    reverse: Option[Boolean] = Some(false),
    round: Option[Boolean] = None
)

object Scale:
  val Point = Scale(`type` = "point", None, None, None, None)
