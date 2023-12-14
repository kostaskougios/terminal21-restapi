package org.terminal21.client.components

import org.terminal21.common.Keys

sealed trait StdElement extends UiElement

case class Text(key: String = Keys.nextKey, var text: String) extends StdElement
case class NewLine(key: String = Keys.nextKey)                extends StdElement

case class Header1(key: String = Keys.nextKey, var text: String) extends StdElement

case class Paragraph(key: String = Keys.nextKey, var text: String = "", var children: Seq[UiElement] = Nil) extends StdElement:
  def withChildren(cn: UiElement*): Paragraph =
    children = cn
    this

  def addChildren(e: UiElement*): Paragraph =
    children = children ++ e
    this
