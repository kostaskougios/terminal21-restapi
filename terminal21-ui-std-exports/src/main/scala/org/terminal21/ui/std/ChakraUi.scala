package org.terminal21.ui.std

import org.terminal21.ui.std.json.chakra.ChakraElement
import org.terminal21.ui.std.model.Session

/** //> exported
  */
trait ChakraUi:
  def element(session: Session, element: ChakraElement): Unit
