package org.terminal21.client.components.nivo

import io.circe.{Encoder, Json}

case class Serie(
    id: String,
    color: String = "hsl(88, 70%, 50%)",
    data: Seq[Datum] = Nil
)

case class Datum(
    x: String | Int | Float,
    y: String | Int | Float
)

object Datum:
  private def toJson(name: String, x: String | Int | Float): (String, Json) = x match
    case s: String => (name, Json.fromString(s))
    case i: Int    => (name, Json.fromInt(i))
    case f: Float  => (name, Json.fromFloat(f).get)

  given Encoder[Datum] =
    case Datum(x, y) =>
      Json.obj(toJson("x", x), toJson("y", y))
