package org.terminal21.client.components

import org.terminal21.client.components.UiElement.HasChildren

/** A UiComponent is a UI element that is composed of a seq of other ui elements
  */
trait UiComponent extends UiElement:
  // Note: impl as a lazy val to avoid UiElements getting a random key
  def rendered: Seq[UiElement]
  override def flat = Seq(this) ++ rendered.flatMap(_.flat)
