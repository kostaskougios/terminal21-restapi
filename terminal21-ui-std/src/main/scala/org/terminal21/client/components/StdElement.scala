package org.terminal21.client.components

import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler}
import org.terminal21.client.{ConnectedSession, OnChangeEventHandler}

sealed trait StdElement extends UiElement

case class Span(key: String = Keys.nextKey, @volatile var text: String) extends StdElement
case class NewLine(key: String = Keys.nextKey)                          extends StdElement
case class Em(key: String = Keys.nextKey, @volatile var text: String)   extends StdElement

case class Header1(key: String = Keys.nextKey, @volatile var text: String) extends StdElement

case class Paragraph(key: String = Keys.nextKey, @volatile var text: String = "", @volatile var children: Seq[UiElement] = Nil)
    extends StdElement
    with HasChildren:
  def withChildren(cn: UiElement*): Paragraph =
    children = cn
    this

  def addChildren(e: UiElement*): Paragraph =
    children = children ++ e
    this

case class Input(key: String = Keys.nextKey, `type`: String = "text", defaultValue: String = "", @volatile var value: String = "")
    extends StdElement
    with HasEventHandler:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

  def onChange(h: OnChangeEventHandler)(using session: ConnectedSession): Input =
    session.addEventHandler(key, h)
    this
