package org.terminal21.client.components

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.components.chakra.{Box, ChakraElement}

class UiElementEncoding(libs: Seq[ComponentLib]):
  given uiElementEncoder: Encoder[UiElement] =
    a =>
      val cl = libs.find(_.toJson.isDefinedAt(a)).getOrElse(throw new IllegalStateException(s"Unknown element $a , did you register a ComponentLib for it?"))
      cl.toJson(a)

object StdElementEncoding extends ComponentLib:

  override def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json] =
    case std: StdElement  => std.asJson.mapObject(o => o.add("type", "Std".asJson))
    case c: ChakraElement => c.asJson.mapObject(o => o.add("type", "Chakra".asJson))
    case c: UiComponent   =>
      val b: ChakraElement = Box(children = c.children)
      b.asJson.mapObject(o => o.add("type", "Chakra".asJson))
