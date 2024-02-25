package org.terminal21.client.components

import org.terminal21.client.components.UiElement.{HasDataStore, HasEventHandler}
import org.terminal21.client.{Model, OnChangeBooleanEventHandlerFunction, OnChangeEventHandlerFunction, OnClickEventHandlerFunction}

trait EventHandler

object OnClickEventHandler:
  trait CanHandleOnClickEvent extends HasDataStore:
    this: UiElement =>
    def onClick[M](using model: Model[M])(h: OnClickEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ClickKey, Nil)
      store(model.ClickKey, handlers :+ h)

object OnChangeEventHandler:
  trait CanHandleOnChangeEvent extends HasDataStore with HasEventHandler:
    this: UiElement =>
    def onChange[M](using model: Model[M])(h: OnChangeEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ChangeKey, Nil)
      store(model.ChangeKey, handlers :+ h)

object OnChangeBooleanEventHandler:
  trait CanHandleOnChangeEvent extends HasDataStore with HasEventHandler:
    this: UiElement =>
    def onChange[M](using model: Model[M])(h: OnChangeBooleanEventHandlerFunction[M]): This =
      val handlers = dataStore.getOrElse(model.ChangeBooleanKey, Nil)
      store(model.ChangeBooleanKey, handlers :+ h)
