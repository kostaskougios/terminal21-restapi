package org.terminal21.client.model

import org.terminal21.client.components.UiElement
import org.terminal21.model.CommandEvent

sealed trait GlobalEvent:
  def isReceivedBy(e: UiElement): Boolean
  def isSessionClose: Boolean

case class UiEvent(event: CommandEvent, receivedBy: UiElement) extends GlobalEvent:
  override def isReceivedBy(e: UiElement): Boolean = e == receivedBy
  override def isSessionClose: Boolean             = false

case object SessionClosedEvent extends GlobalEvent:
  override def isReceivedBy(e: UiElement): Boolean = false
  override def isSessionClose: Boolean             = true
