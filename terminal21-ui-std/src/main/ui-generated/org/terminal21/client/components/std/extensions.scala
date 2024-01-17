
package org.terminal21.client.components.std

// GENERATED WITH PropertiesExtensionGenerator, DON'T EDIT

import org.terminal21.client.components.UiElement.HasChildren
import org.terminal21.client.components.UiElement.HasEventHandler
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.Keys
import org.terminal21.client.components.UiElement
import org.terminal21.client.ConnectedSession
import org.terminal21.client.OnChangeEventHandler

extension (e: NewLine)
  def withKey(v: String) = e.copy(key = v)
  def withStyle(v: Map[String, Any]) = e.copy(style = v)


extension (e: Header1)
  def withKey(v: String) = e.copy(key = v)
  def withText(v: String) = e.copy(text = v)
  def withStyle(v: Map[String, Any]) = e.copy(style = v)


extension (e: Span)
  def withKey(v: String) = e.copy(key = v)
  def withText(v: String) = e.copy(text = v)
  def withStyle(v: Map[String, Any]) = e.copy(style = v)


extension (e: Paragraph)
  def withKey(v: String) = e.copy(key = v)
  def withText(v: String) = e.copy(text = v)
  def withStyle(v: Map[String, Any]) = e.copy(style = v)
  def withChildren(v: Seq[UiElement]) = e.copy(children = v)




extension (e: Em)
  def withKey(v: String) = e.copy(key = v)
  def withText(v: String) = e.copy(text = v)
  def withStyle(v: Map[String, Any]) = e.copy(style = v)


extension (e: Input)
  def withKey(v: String) = e.copy(key = v)
  def withType(v: String) = e.copy(`type` = v)
  def withDefaultValue(v: Option[String]) = e.copy(defaultValue = v)
  def withStyle(v: Map[String, Any]) = e.copy(style = v)
  def withValue(v: Option[String]) = e.copy(value = v)

