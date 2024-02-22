package org.terminal21.client.model

import org.terminal21.client.components.UiElement
import org.terminal21.model.{CommandEvent, OnChange, OnClick}

/** These are the events as handled by the std lib. They enrich the server events with local information.
  */
sealed trait GlobalEvent:
  def isTarget(e: UiElement): Boolean
  def isSessionClose: Boolean

object GlobalEvent:
  def onClick(receivedBy: UiElement): UiEvent                  = UiEvent(OnClick(receivedBy.key), receivedBy)
  def onChange(receivedBy: UiElement, value: String): UiEvent  = UiEvent(OnChange(receivedBy.key, value), receivedBy)
  def onChange(receivedBy: UiElement, value: Boolean): UiEvent = UiEvent(OnChange(receivedBy.key, value.toString), receivedBy)
  def sessionClosed: GlobalEvent                               = SessionClosedEvent

case class UiEvent(event: CommandEvent, receivedBy: UiElement) extends GlobalEvent:
  override def isTarget(e: UiElement): Boolean = e.key == receivedBy.key
  override def isSessionClose: Boolean         = false

case object SessionClosedEvent extends GlobalEvent:
  override def isTarget(e: UiElement): Boolean = false
  override def isSessionClose: Boolean         = true
