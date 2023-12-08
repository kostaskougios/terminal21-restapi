package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.terminal21.client.json.{Header1, Paragraph, StdElement}
import org.terminal21.client.ui.{UiElement, UiLib}
import org.terminal21.common.Keys

class Std(session: ConnectedSession) extends UiLib:
  def paragraph(text: String, key: String = Keys.nextKey): Unit =
    session.add(Paragraph(key, text))

  def header1(text: String, key: String = Keys.nextKey): Unit =
    session.add(Header1(key, text))

  override def toJson(e: UiElement) = e match
    case std: StdElement => Some(std.asJson.mapObject(o => o.add("type", "Std".asJson)))
    case _               => None

object Std:
  given (ConnectedSession => Std) = session => new Std(session)
