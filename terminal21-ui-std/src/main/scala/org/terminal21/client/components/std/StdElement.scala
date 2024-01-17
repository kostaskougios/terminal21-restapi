package org.terminal21.client.components.std

import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler, HasStyle}
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.client.{ConnectedSession, OnChangeEventHandler}

sealed trait StdEJson                   extends UiElement
sealed trait StdElement[A <: UiElement] extends StdEJson with HasStyle[A]

case class Span(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Span]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class NewLine(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends StdElement[NewLine]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class Em(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Em]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class Header1(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header1]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class Paragraph(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends StdElement[Paragraph]
    with HasChildren[Paragraph]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    defaultValue: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    value: Option[String] = None
) extends StdElement[Input]
    with HasEventHandler:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(value = Some(newValue)))
  override def style(v: Map[String, Any])                                           = copy(style = v)

  def onChange(h: OnChangeEventHandler)(using session: ConnectedSession): Input =
    session.addEventHandler(key, h)
    this
