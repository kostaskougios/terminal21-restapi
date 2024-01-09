package org.terminal21.client.components.nivo

case class Serie(
    id: String,
    color: String = "hsl(88, 70%, 50%)",
    data: Seq[Datum] = Nil
)

case class Datum(
    x: String,
    y: String
)
