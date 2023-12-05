package org.terminal21.ui.std.json

sealed trait StdElement:
  def key: String

case class Header1(key: String, text: String)   extends StdElement
case class Paragraph(key: String, text: String) extends StdElement
