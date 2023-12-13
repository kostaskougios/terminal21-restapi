package org.terminal21.client.json

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.json.chakra.ChakraElement
import org.terminal21.client.ui.UiElement

object UiElementEncoding:
  given uiElementEncoder: Encoder[UiElement] =
    case std: StdElement  => std.asJson.mapObject(o => o.add("type", "Std".asJson))
    case c: ChakraElement => c.asJson.mapObject(o => o.add("type", "Chakra".asJson))
