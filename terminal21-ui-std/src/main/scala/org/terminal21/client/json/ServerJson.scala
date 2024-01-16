package org.terminal21.client.json

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.HasChildren

case class ServerJson(
    rootKeys: Seq[String],
    elements: Map[String, UiElement],
    keyTree: Map[String, Seq[String]]
)

object ServerJson:
  def from(elements: Seq[UiElement]): ServerJson =
    val flat = elements.flatMap(_.flat)
    ServerJson(
      elements.map(_.key),
      flat
        .map: el =>
          (
            el.key,
            el match
              case e: HasChildren[_] => e.copyNoChildren
              case e                 => e
          )
        .toMap,
      flat
        .map: e =>
          (
            e.key,
            e match
              case e: HasChildren[_] => e.children.map(_.key)
              case _                 => Nil
          )
        .toMap
    )
