package org.terminal21.client.components

import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.*
import org.terminal21.client.components.nivo.NEJson
import org.terminal21.client.components.{ComponentLib, UiElement}

object NivoLib extends ComponentLib:
  import org.terminal21.client.components.StdElementEncoding.given
  override def toJson(using Encoder[UiElement]): PartialFunction[UiElement, Json] =
    case n: NEJson => n.asJson.mapObject(o => o.add("type", "Nivo".asJson))
