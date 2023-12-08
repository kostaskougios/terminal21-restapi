package org.terminal21.client

import org.terminal21.common.Keys
import org.terminal21.ui.std.json.chakra.Button
import org.terminal21.ui.std.{ChakraUi, ChakraUiCallerFactory}

class Chakra(session: ConnectedSession, chakraUi: ChakraUi):
  def button(text: String, key: String = Keys.randomKey): Unit =
    chakraUi.element(session.session, Button(key, text))

object Chakra:
  given (ConnectedSession => Chakra) = session => new Chakra(session, ChakraUiCallerFactory.newHelidonJsonChakraUi(session.transport))
