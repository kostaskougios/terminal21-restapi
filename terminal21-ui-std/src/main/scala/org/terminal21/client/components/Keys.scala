package org.terminal21.client.components

import org.terminal21.client.components.UiElement.HasChildren

object Keys:
  def nextKey: String = ""

  def linearKeys(parentKey: String, element: UiElement): Seq[UiElement]               = linearKeys(parentKey, Seq(element))
  def linearKeys(elements: Seq[UiElement]): Seq[UiElement]                            = linearKeys(None, elements)
  def linearKeys(parentKey: String, elements: Seq[UiElement]): Seq[UiElement]         = linearKeys(Some(parentKey), elements)
  def linearKeys(parentKey: Option[String], elements: Seq[UiElement]): Seq[UiElement] =
    val pk = parentKey.map(_ + "-").getOrElse("k-")
    elements.zipWithIndex.map:
      case (e, i) =>
        val p = if e.key.isEmpty then e.withKey(s"$pk$i") else e
        p match
          case wc: HasChildren => wc.withChildren(linearKeys(Some(p.key), wc.children)*)
          case n               => n
