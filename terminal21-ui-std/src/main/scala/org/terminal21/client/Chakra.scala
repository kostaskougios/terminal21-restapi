package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.json.chakra.{Box, Button, ChakraElement, ChakraProps}
import org.terminal21.client.ui.{UiElement, UiLib}
import org.terminal21.common.Keys

class Chakra(session: ConnectedSession) extends UiLib:
  def button(text: String, key: String = Keys.nextKey)(clickEventHandler: OnClickEventHandler): Unit =
    session.addEventHandler(key, clickEventHandler)
    session.add(Button(key, text))

  def box(text: String, props: ChakraProps = ChakraProps(), key: String = Keys.nextKey): Unit =
    session.add(Box(key, text, props))

  override def toJson(e: UiElement) = e match
    case e: ChakraElement => Some(e.asJson.mapObject(o => o.add("type", "Chakra".asJson)))
    case _                => None

object Chakra:
  given (ConnectedSession => Chakra) = session => new Chakra(session)
