package org.terminal21.client.components.std

import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.UiElement.{HasChildren, HasStyle}
import org.terminal21.client.components.{Keys, OnChangeEventHandler, UiElement}
import org.terminal21.collections.TypedMap

sealed trait StdEJson   extends UiElement
sealed trait StdElement extends StdEJson with HasStyle

case class Span(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Span
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class NewLine(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = NewLine
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Em(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Em
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Header1(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Header1
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Header2(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Header2
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Header3(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Header3
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Header4(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Header4
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Header5(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Header5
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Header6(key: String = Keys.nextKey, text: String, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends StdElement:
  type This = Header6
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Paragraph(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends StdElement
    with HasChildren:
  type This = Paragraph
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    defaultValue: String = "",
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends StdElement
    with CanHandleOnChangeEvent:
  type This = Input
  override def withStyle(v: Map[String, Any])     = copy(style = v)
  def withKey(v: String)                          = copy(key = v)
  def withType(v: String)                         = copy(`type` = v)
  def withDefaultValue(v: String)                 = copy(defaultValue = v)
  override def withDataStore(ds: TypedMap): Input = copy(dataStore = ds)
