package org.terminal21.model

sealed trait CommandEvent

case class OnClick(key: String) extends CommandEvent
