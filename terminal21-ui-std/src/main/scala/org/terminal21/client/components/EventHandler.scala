package org.terminal21.client.components

import org.terminal21.client.{Model, OnChangeBooleanEventHandlerFunction, OnChangeEventHandlerFunction, OnClickEventHandlerFunction}

trait EventHandler

object OnClickEventHandler:
  trait CanHandleOnClickEvent:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"clickables must have a stable key. Error occurred on $this")
    def onClick[M](model: Model[M])(h: OnClickEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ClickEventHandlerKey, Nil)
      store(model.ClickEventHandlerKey, handlers :+ h)

object OnChangeEventHandler:
  trait CanHandleOnChangeEvent:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"changeable must have a stable key. Error occurred on $this")
    def onChange[M](model: Model[M])(h: OnChangeEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ChangeEventHandlerKey, Nil)
      store(model.ChangeEventHandlerKey, handlers :+ h)

object OnChangeBooleanEventHandler:
  trait CanHandleOnChangeEvent:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"changeable must have a stable key. Error occurred on $this")
    def onChange[M](model: Model[M])(h: OnChangeBooleanEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ChangeBooleanEventHandlerKey, Nil)
      store(model.ChangeBooleanEventHandlerKey, handlers :+ h)
