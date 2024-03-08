package org.terminal21.client.components.frontend

import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.collections.TypedMap

sealed trait FrontEndElement extends UiElement

case class ThemeToggle(key: String = Keys.nextKey, dataStore: TypedMap = TypedMap.Empty) extends FrontEndElement:
  override type This = ThemeToggle
  override def withKey(key: String): ThemeToggle = copy(key = key)
  override def withDataStore(ds: TypedMap)       = copy(dataStore = ds)
