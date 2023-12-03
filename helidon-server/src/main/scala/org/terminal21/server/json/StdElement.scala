package org.terminal21.server.json

sealed trait StdElement

case class Paragraph(text: String) extends StdElement
