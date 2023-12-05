package org.terminal21.ui.std.json.chakra

import org.terminal21.ui.common.UiElement

sealed trait ChakraElement extends UiElement

case class Button(key: String, text: String) extends ChakraElement
