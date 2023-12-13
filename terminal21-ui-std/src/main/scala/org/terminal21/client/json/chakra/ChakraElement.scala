package org.terminal21.client.json.chakra

import org.terminal21.client.ui.UiElement

sealed trait ChakraElement extends UiElement

case class Button(key: String, text: String)                  extends ChakraElement
case class Box(key: String, text: String, props: ChakraProps) extends ChakraElement
