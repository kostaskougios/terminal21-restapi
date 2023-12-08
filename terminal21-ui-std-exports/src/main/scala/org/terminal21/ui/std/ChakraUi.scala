package org.terminal21.ui.std

import org.terminal21.model.Session
import org.terminal21.ui.std.json.chakra.ChakraElement

/** //> exported
  */
trait ChakraUi:
  def element(session: Session, element: ChakraElement): Unit
