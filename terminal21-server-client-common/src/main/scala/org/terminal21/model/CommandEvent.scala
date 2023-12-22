package org.terminal21.model

sealed trait CommandEvent:
  def key: String

case class OnClick(key: String)                 extends CommandEvent
case class OnChange(key: String, value: String) extends CommandEvent

case class SessionClosed(key: String) extends CommandEvent
