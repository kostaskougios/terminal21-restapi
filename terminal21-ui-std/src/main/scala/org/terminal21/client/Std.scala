package org.terminal21.client

import org.terminal21.ui.std.json.{Header1, Paragraph}
import org.terminal21.ui.std.model.Session
import org.terminal21.ui.std.{StdUi, StdUiCallerFactory}

class Std(session: Session, stdUi: StdUi):
  def paragraph(text: String): Unit =
    stdUi.elements(session, Seq(Paragraph(text)))

  def header1(text: String): Unit =
    stdUi.elements(session, Seq(Header1(text)))

object Std:
  def newStd(session: ConnectedSession): Std =
    val stdUi = StdUiCallerFactory.newHelidonJsonStdUi(session.transport)
    new Std(session.session, stdUi)
