package org.terminal21.ui.std.json

import org.terminal21.ui.common.UiElement

sealed trait StdElement extends UiElement

case class Header1(key: String, text: String)   extends StdElement
case class Paragraph(key: String, text: String) extends StdElement
