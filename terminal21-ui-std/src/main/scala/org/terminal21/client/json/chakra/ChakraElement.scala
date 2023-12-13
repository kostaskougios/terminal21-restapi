package org.terminal21.client.json.chakra

import org.terminal21.client.ui.UiElement
import org.terminal21.common.Keys

sealed trait ChakraElement extends UiElement

case class Button(key: String = Keys.nextKey, text: String)                  extends ChakraElement
case class Box(key: String = Keys.nextKey, text: String, props: ChakraProps) extends ChakraElement
