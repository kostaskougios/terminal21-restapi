package org.terminal21.ui.std

import org.terminal21.model.Session
import org.terminal21.ui.std.json.StdElement

/** //> exported
  */
trait StdUi:
  def element(session: Session, element: StdElement): Unit
