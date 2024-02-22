package org.terminal21.model

import org.terminal21.client.components.AnyElement

/** These are the events as they arrive from the server
  */
sealed trait CommandEvent:
  def key: String

object CommandEvent:
  def onClick(receivedBy: AnyElement): OnClick                   = OnClick(receivedBy.key)
  def onChange(receivedBy: AnyElement, value: String): OnChange  = OnChange(receivedBy.key, value)
  def onChange(receivedBy: AnyElement, value: Boolean): OnChange = OnChange(receivedBy.key, value.toString)
  def sessionClosed: SessionClosed                               = SessionClosed("-")

case class OnClick(key: String)                 extends CommandEvent
case class OnChange(key: String, value: String) extends CommandEvent

case class SessionClosed(key: String) extends CommandEvent
