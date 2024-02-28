package org.terminal21.client.components

import org.terminal21.client.components.UiElement.HasDataStore
import org.terminal21.client.{Model, OnChangeBooleanEventHandlerFunction, OnChangeEventHandlerFunction, OnClickEventHandlerFunction}

trait EventHandler

object OnClickEventHandler:
  trait CanHandleOnClickEvent extends HasDataStore:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"clickables must have a stable key. Error occurred on $this")
    def onClick[M](using model: Model[M])(h: OnClickEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ClickKey, Nil)
      store(model.ClickKey, handlers :+ h)

object OnChangeEventHandler:
  trait CanHandleOnChangeEvent extends HasDataStore:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"changeable must have a stable key. Error occurred on $this")
    def onChange[M](using model: Model[M])(h: OnChangeEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ChangeKey, Nil)
      store(model.ChangeKey, handlers :+ h)

object OnChangeBooleanEventHandler:
  trait CanHandleOnChangeEvent extends HasDataStore:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"changeable must have a stable key. Error occurred on $this")
    def onChange[M](using model: Model[M])(h: OnChangeBooleanEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ChangeBooleanKey, Nil)
      store(model.ChangeBooleanKey, handlers :+ h)
