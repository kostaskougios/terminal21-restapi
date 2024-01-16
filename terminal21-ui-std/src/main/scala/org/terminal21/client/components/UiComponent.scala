package org.terminal21.client.components

import org.terminal21.client.components.UiElement.HasChildren

/** A UiComponent is a UI element that is composed of a seq of other ui elements
  */
trait UiComponent extends UiElement with HasChildren[UiComponent]:
  override def copyNoChildren: UiComponent = ???
