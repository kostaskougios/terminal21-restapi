package org.terminal21.client.components.std

import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler, HasStyle}
import org.terminal21.client.components.{Keys, OnChangeEventHandler, UiElement}
import org.terminal21.client.ConnectedSession
import org.terminal21.collections.TypedMap

sealed trait StdEJson                   extends UiElement
sealed trait StdElement[A <: UiElement] extends StdEJson with HasStyle[A]

case class Span(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Span]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class NewLine(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends StdElement[NewLine]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class Em(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Em]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Header1(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header1]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Header2(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header2]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Header3(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header3]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Header4(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header4]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Header5(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header5]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Header6(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty) extends StdElement[Header6]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Paragraph(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends StdElement[Paragraph]
    with HasChildren[Paragraph]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    defaultValue: String = "",
    style: Map[String, Any] = Map.empty,
    valueReceived: Option[String] = None, // use value instead
    dataStore: TypedMap = TypedMap.empty
) extends StdElement[Input]
    with CanHandleOnChangeEvent[Input]:
  override def defaultEventHandler                = newValue => copy(valueReceived = Some(newValue))
  override def withStyle(v: Map[String, Any])     = copy(style = v)
  def withKey(v: String)                          = copy(key = v)
  def withType(v: String)                         = copy(`type` = v)
  def withDefaultValue(v: String)                 = copy(defaultValue = v)
  def value                                       = valueReceived.getOrElse(defaultValue)
  override def withDataStore(ds: TypedMap): Input = copy(dataStore = ds)
