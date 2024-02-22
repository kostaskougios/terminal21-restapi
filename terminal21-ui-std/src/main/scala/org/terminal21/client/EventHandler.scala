package org.terminal21.client

import org.terminal21.client.collections.TypedMapKey
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.HasDataStore
import org.terminal21.client.model.UiEvent

trait EventHandler

trait OnClickEventHandler extends EventHandler:
  def onClick(): Unit

object OnClickEventHandler:
  object Key extends TypedMapKey[Seq[OnClickEventHandler]]

  trait CanHandleOnClickEvent[A <: UiElement] extends HasDataStore[A]:
    this: A =>
    def onClick(h: OnClickEventHandler): A =
      val handlers = dataStore.getOrElse(Key, Nil)
      store(Key, handlers :+ h)

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
  def onEvent(event: UiEvent): Unit
