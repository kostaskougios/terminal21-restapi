package org.terminal21.client.components.mathjax

import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.*
import org.terminal21.client.components.{ComponentLib, UiElement}

object MathJaxLib extends ComponentLib:
  import org.terminal21.client.json.StdElementEncoding.given
  override def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json] =
    case n: MathJaxElement => n.asJson.mapObject(o => o.add("type", "MathJax".asJson))
