package org.terminal21.client.components.nivo

import io.circe.{Encoder, Json}
import org.terminal21.client.components.{ComponentLib, UiElement}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*

object NivoLib extends ComponentLib:
  import org.terminal21.client.components.StdElementEncoding.given
  override def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json] =
    case n: NEJson => n.asJson.mapObject(o => o.add("type", "Nivo".asJson))
