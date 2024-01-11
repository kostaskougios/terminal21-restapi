package org.terminal21.client.components.nivo

import io.circe.{Encoder, Json}
import org.terminal21.client.components.{ComponentLib, UiElement}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

object NivoLib extends ComponentLib:
  given Encoder[Map[String, Any]] = m =>
    val vs = m.toSeq.map: (k, v) =>
      (
        k,
        v match
          case s: String => Json.fromString(s)
          case i: Int    => Json.fromInt(i)
          case f: Float  => Json.fromFloat(f).get
          case d: Double => Json.fromDouble(d).get
          case _         => throw new IllegalArgumentException(s"type $v not supported, either use one of the supported ones or open a bug request")
      )
    Json.obj(vs: _*)

  override def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json] =
    case n: NivoElement => n.asJson.mapObject(o => o.add("type", "Nivo".asJson))
