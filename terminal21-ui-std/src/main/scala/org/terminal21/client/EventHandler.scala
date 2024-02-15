package org.terminal21.client

import org.terminal21.client.components.UiElement
import org.terminal21.model.CommandEvent

trait EventHandler

trait OnClickEventHandler extends EventHandler:
  def onClick(): Unit

object OnClickEventHandler:
  trait CanHandleOnClickEvent[A <: UiElement]:
    this: A =>
    def onClick(h: OnClickEventHandler)(using session: ConnectedSession): A =
      session.addEventHandler(key, h)
      this

trait OnChangeEventHandler extends EventHandler:
  def onChange(newValue: String): Unit

object OnChangeEventHandler:
  trait CanHandleOnChangeEvent[A <: UiElement]:
    this: A =>
    def onChange(h: OnChangeEventHandler)(using session: ConnectedSession): A =
      session.addEventHandler(key, h)
      this

trait OnChangeBooleanEventHandler extends EventHandler:
  def onChange(newValue: Boolean): Unit

object OnChangeBooleanEventHandler:
  trait CanHandleOnChangeEvent[A <: UiElement]:
    this: A =>
    def onChange(h: OnChangeBooleanEventHandler)(using session: ConnectedSession): A =
      session.addEventHandler(key, h)
      this

trait GlobalEventHandler extends EventHandler:
  def onEvent(event: CommandEvent): Unit
