package org.terminal21.client.components.nivo

case class Serie(
    id: String,
    color: String = "hsl(88, 70%, 50%)",
    data: Seq[Datum] = Nil
)

sealed trait Datum

case class StringDatum(
    x: String,
    y: String
) extends Datum

case class IntDatum(
    x: Int,
    y: Int
) extends Datum

case class FloatDatum(
    x: Float,
    y: Float
) extends Datum

case class StringIntDatum(
    x: String,
    y: Int
) extends Datum
