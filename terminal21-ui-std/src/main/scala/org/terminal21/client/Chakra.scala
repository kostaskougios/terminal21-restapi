package org.terminal21.client

import org.terminal21.client.json.chakra.{Box, Button, ChakraProps}
import org.terminal21.client.ui.UiLib
import org.terminal21.common.Keys

class Chakra(session: ConnectedSession) extends UiLib:
  def button(text: String, key: String = Keys.nextKey)(clickEventHandler: OnClickEventHandler): Unit =
    session.addEventHandler(key, clickEventHandler)
    session.add(Button(key, text))

  def box(text: String, props: ChakraProps = ChakraProps(), key: String = Keys.nextKey): Unit =
    session.add(Box(key, text, props))

object Chakra:
  given (ConnectedSession => Chakra) = session => new Chakra(session)
