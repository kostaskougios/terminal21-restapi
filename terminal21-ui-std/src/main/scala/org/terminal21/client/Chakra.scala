package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.json.chakra.{Button, ChakraElement}
import org.terminal21.client.ui.{UiElement, UiLib}
import org.terminal21.common.Keys

class Chakra(session: ConnectedSession) extends UiLib:
  def button(text: String, key: String = Keys.randomKey): Unit =
    session.add(Button(key, text))

  override def toJson(e: UiElement) = e match
    case e: ChakraElement => Some(e.asJson.mapObject(o => o.add("type", "Chakra".asJson)))
    case _                => None

object Chakra:
  given (ConnectedSession => Chakra) = session => new Chakra(session)
