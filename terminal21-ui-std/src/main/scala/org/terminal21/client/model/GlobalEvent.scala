package org.terminal21.client.model

import org.terminal21.client.components.UiElement
import org.terminal21.model.CommandEvent

sealed trait GlobalEvent:
  def isTarget(e: UiElement): Boolean
  def isSessionClose: Boolean

case class UiEvent(event: CommandEvent, receivedBy: UiElement) extends GlobalEvent:
  override def isTarget(e: UiElement): Boolean = e.key == receivedBy.key
  override def isSessionClose: Boolean         = false

case object SessionClosedEvent extends GlobalEvent:
  override def isTarget(e: UiElement): Boolean = false
  override def isSessionClose: Boolean         = true
