package org.terminal21.client

import org.terminal21.client.json.{Header1, Paragraph}
import org.terminal21.client.ui.UiLib
import org.terminal21.common.Keys

class Std(session: ConnectedSession) extends UiLib:
  def paragraph(text: String, key: String = Keys.nextKey): Unit =
    session.add(Paragraph(key, text))

  def header1(text: String, key: String = Keys.nextKey): Unit =
    session.add(Header1(key, text))

object Std:
  given (ConnectedSession => Std) = session => new Std(session)
