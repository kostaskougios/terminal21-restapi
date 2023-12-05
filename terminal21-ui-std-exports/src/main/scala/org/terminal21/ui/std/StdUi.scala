package org.terminal21.ui.std

import org.terminal21.ui.std.json.StdElement
import org.terminal21.ui.std.model.Session

/** //> exported
  */
trait StdUi:
  def element(session: Session, element: StdElement): Unit
