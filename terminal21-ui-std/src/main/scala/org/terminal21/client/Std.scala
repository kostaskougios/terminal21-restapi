package org.terminal21.client

import org.terminal21.common.Keys
import org.terminal21.ui.std.json.{Header1, Paragraph, StdElement}
import org.terminal21.ui.std.model.Session
import org.terminal21.ui.std.{StdUi, StdUiCallerFactory}

class Std(session: Session, stdUi: StdUi):
  def paragraph(text: String): Unit =
    paragraph(Keys.randomKey, text)

  def paragraph(key: String, text: String): Unit =
    add(Paragraph(key, text))

  def header1(text: String): Unit = header1(Keys.randomKey, text)

  def header1(key: String, text: String): Unit =
    add(Header1(key, text))

  private def add(e: StdElement) = stdUi.element(session, e)

object Std:
  def newStd(session: ConnectedSession): Std =
    val stdUi = StdUiCallerFactory.newHelidonJsonStdUi(session.transport)
    new Std(session.session, stdUi)
