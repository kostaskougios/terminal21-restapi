package org.terminal21.client.components

import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler, HasStyle}
import org.terminal21.client.{ConnectedSession, OnChangeEventHandler}

sealed trait StdElement extends UiElement with HasStyle

case class Span(key: String = Keys.nextKey, @volatile var text: String, @volatile var style: Map[String, Any] = Map.empty) extends StdElement
case class NewLine(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty)                          extends StdElement
case class Em(key: String = Keys.nextKey, @volatile var text: String, @volatile var style: Map[String, Any] = Map.empty)   extends StdElement

case class Header1(key: String = Keys.nextKey, @volatile var text: String, @volatile var style: Map[String, Any] = Map.empty) extends StdElement

case class Paragraph(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends StdElement
    with HasChildren[Paragraph]:
  override def copyNoChildren: Paragraph = copy(children = Nil)

case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    defaultValue: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var value: String = ""
) extends StdElement
    with HasEventHandler:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

  def onChange(h: OnChangeEventHandler)(using session: ConnectedSession): Input =
    session.addEventHandler(key, h)
    this
