package org.terminal21.ui.std.json

sealed trait StdElement

case class Header1(text: String)   extends StdElement
case class Paragraph(text: String) extends StdElement
