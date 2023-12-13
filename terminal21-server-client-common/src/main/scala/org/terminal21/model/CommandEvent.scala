package org.terminal21.model

sealed trait CommandEvent:
  def key: String

case class OnClick(key: String) extends CommandEvent
