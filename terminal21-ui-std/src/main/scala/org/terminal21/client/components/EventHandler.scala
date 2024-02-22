package org.terminal21.client.components

import org.terminal21.client.components.UiElement.{HasDataStore, HasEventHandler}
import org.terminal21.client.{Model, OnChangeBooleanEventHandlerFunction, OnChangeEventHandlerFunction, OnClickEventHandlerFunction}

trait EventHandler

object OnClickEventHandler:
  trait CanHandleOnClickEvent[A <: UiElement] extends HasDataStore[A]:
    this: A =>
    def onClick[M](using model: Model[M])(h: OnClickEventHandlerFunction[M]): A =
      val handlers = dataStore.getOrElse(model.ClickKey, Nil)
      store(model.ClickKey, handlers :+ h)

object OnChangeEventHandler:
  trait CanHandleOnChangeEvent[A <: UiElement] extends HasDataStore[A] with HasEventHandler[A]:
    this: A =>
    def onChange[M](using model: Model[M])(h: OnChangeEventHandlerFunction[M]): A =
      val handlers = dataStore.getOrElse(model.ChangeKey, Nil)
      store(model.ChangeKey, handlers :+ h)

object OnChangeBooleanEventHandler:
  trait CanHandleOnChangeEvent[A <: UiElement] extends HasDataStore[A] with HasEventHandler[A]:
    this: A =>
    def onChange[M](using model: Model[M])(h: OnChangeBooleanEventHandlerFunction[M]): A =
      val handlers = dataStore.getOrElse(model.ChangeBooleanKey, Nil)
      store(model.ChangeBooleanKey, handlers :+ h)
