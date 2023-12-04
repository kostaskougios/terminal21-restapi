package org.terminal21.server.json

sealed trait StdElement

case class Header1(text: String)   extends StdElement
case class Paragraph(text: String) extends StdElement
