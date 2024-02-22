package org.terminal21.client.components.frontend

import org.terminal21.client.components.{Keys, UiElement}

sealed trait FrontEndElement extends UiElement

case class ThemeToggle(key: String = Keys.nextKey) extends FrontEndElement
