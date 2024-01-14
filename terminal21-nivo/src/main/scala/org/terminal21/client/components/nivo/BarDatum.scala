package org.terminal21.client.components.nivo

import io.circe.{Encoder, Json}

case class BarDatum(
    name: String,
    value: Any // String, Int, Float etc, see the encoder below for supported types
)

object BarDatum:
  given Encoder[Seq[BarDatum]] = s =>
    val vs = s.map: bd =>
      (
        bd.name,
        bd.value match
          case s: String  => Json.fromString(s)
          case i: Int     => Json.fromInt(i)
          case f: Float   => Json.fromFloat(f).get
          case d: Double  => Json.fromDouble(d).get
          case b: Boolean => Json.fromBoolean(b)
          case _          => throw new IllegalArgumentException(s"type $bd not supported, either use one of the supported ones or open a bug request")
      )
    Json.obj(vs: _*)
