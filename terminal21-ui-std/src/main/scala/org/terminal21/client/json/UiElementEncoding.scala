package org.terminal21.client.json

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.components.chakra.{Box, CEJson, ChakraElement}
import org.terminal21.client.components.frontend.FrontEndElement
import org.terminal21.client.components.std.{StdEJson, StdElement, StdHttp}
import org.terminal21.client.components.{ComponentLib, UiComponent, UiElement}
import org.terminal21.collections.TypedMap

class UiElementEncoding(libs: Seq[ComponentLib]):
  given uiElementEncoder: Encoder[UiElement] =
    a =>
      val cl =
        libs
          .find(_.toJson.isDefinedAt(a))
          .getOrElse(throw new IllegalStateException(s"Unknown ui element, did you forget to register a Lib when creating a session? Component:  $a"))
      cl.toJson(a)

object StdElementEncoding extends ComponentLib:
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

  given Encoder[TypedMap] = _ => Json.Null

  override def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json] =
    case std: StdEJson       => std.asJson.mapObject(o => o.add("type", "Std".asJson))
    case c: CEJson           => c.asJson.mapObject(o => o.add("type", "Chakra".asJson))
    case c: UiComponent      =>
      val b: ChakraElement = Box(key = c.key, text = "")
      b.asJson.mapObject(o => o.add("type", "Chakra".asJson))
    case std: StdHttp        => std.asJson.mapObject(o => o.add("type", "Std".asJson))
    case fe: FrontEndElement => fe.asJson.mapObject(o => o.add("type", "FrontEnd".asJson))
