package org.terminal21.client

import org.terminal21.common.Keys
import org.terminal21.ui.std.json.{Header1, Paragraph, StdElement}
import org.terminal21.ui.std.{StdUi, StdUiCallerFactory}

class Std(session: ConnectedSession, stdUi: StdUi):

  def paragraph(text: String, key: String = Keys.randomKey): Unit =
    add(Paragraph(key, text))

  def header1(text: String, key: String = Keys.randomKey): Unit =
    add(Header1(key, text))

  private def add(e: StdElement) = stdUi.element(session.session, e)

object Std:
  given (ConnectedSession => Std) = session =>
    val stdUi = StdUiCallerFactory.newHelidonJsonStdUi(session.transport)
    new Std(session, stdUi)
