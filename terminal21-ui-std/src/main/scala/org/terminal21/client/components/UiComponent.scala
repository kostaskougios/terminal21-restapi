package org.terminal21.client.components

/** A UiComponent is a UI element that is composed of a seq of other ui elements
  */
trait UiComponent extends UiElement:
  // Note: impl as a lazy val to avoid UiElements getting a random key and try to fix the
  // keys of any sub-elements the component has.
  def rendered: Seq[UiElement]
  override def flat = Seq(this) ++ rendered.flatMap(_.flat)

  protected def subKey(suffix: String): String = if key.isEmpty then "" else key + "-" + suffix
