package org.terminal21.client.json.chakra

import org.terminal21.client.ui.UiElement
import org.terminal21.common.Keys

sealed trait ChakraElement extends UiElement

case class Button(key: String = Keys.nextKey, text: String)                                                              extends ChakraElement
case class Box(key: String = Keys.nextKey, var text: String, var props: ChakraProps, var children: Seq[UiElement] = Nil) extends ChakraElement:
  def withChildren(cn: UiElement*): Box =
    children = cn
    this
