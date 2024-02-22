package org.terminal21.client

import org.terminal21.client.collections.TypedMapKey
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.{HasDataStore, HasEventHandler}
import org.terminal21.client.model.UiEvent

trait EventHandler

trait OnClickEventHandler extends EventHandler:
  def onClick(): Unit

object OnClickEventHandler:
  object Key                                  extends TypedMapKey[Seq[OnClickEventHandler]]
  trait CanHandleOnClickEvent[A <: UiElement] extends HasDataStore[A]:
    this: A =>
    def onClick(h: OnClickEventHandler): A =
      val handlers = dataStore.getOrElse(Key, Nil)
      store(Key, handlers :+ h)

trait OnChangeEventHandler extends EventHandler:
  def onChange(newValue: String): Unit

object OnChangeEventHandler:
  object Key                                   extends TypedMapKey[Seq[OnChangeEventHandler]]
  trait CanHandleOnChangeEvent[A <: UiElement] extends HasDataStore[A] with HasEventHandler:
    this: A =>
    def onChange(h: OnChangeEventHandler): A =
      val handlers = dataStore.getOrElse(Key, Nil)
      store(Key, handlers :+ h)

trait OnChangeBooleanEventHandler extends EventHandler:
  def onChange(newValue: Boolean): Unit

object OnChangeBooleanEventHandler:
  object Key                                   extends TypedMapKey[Seq[OnChangeBooleanEventHandler]]
  trait CanHandleOnChangeEvent[A <: UiElement] extends HasDataStore[A] with HasEventHandler:
    this: A =>
    def onChange(h: OnChangeBooleanEventHandler): A =
      val handlers = dataStore.getOrElse(Key, Nil)
      store(Key, handlers :+ h)

trait GlobalEventHandler extends EventHandler:
  def onEvent(event: UiEvent): Unit
