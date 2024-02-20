package org.terminal21.client.model

import org.terminal21.client.components.UiElement
import org.terminal21.model.{CommandEvent, OnChange, OnClick}

sealed trait GlobalEvent:
  def isTarget(e: UiElement): Boolean
  def isSessionClose: Boolean

object GlobalEvent:
  def onClick(receivedBy: UiElement): UiEvent                  = UiEvent(OnClick(receivedBy.key), receivedBy)
  def onChange(receivedBy: UiElement, value: String): UiEvent  = UiEvent(OnChange(receivedBy.key, value), receivedBy)
  def onChangeEvent(receivedBy: UiElement, value: Boolean): UiEvent = UiEvent(OnChange(receivedBy.key, value.toString), receivedBy)
  def sessionClosedEvent: GlobalEvent                               = SessionClosedEvent

case class UiEvent(event: CommandEvent, receivedBy: UiElement) extends GlobalEvent:
  override def isTarget(e: UiElement): Boolean = e.key == receivedBy.key
  override def isSessionClose: Boolean         = false

case object SessionClosedEvent extends GlobalEvent:
  override def isTarget(e: UiElement): Boolean = false
  override def isSessionClose: Boolean         = true
