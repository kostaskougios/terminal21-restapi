package org.terminal21.client.components

trait EventHandler

object OnClickEventHandler:
  trait CanHandleOnClickEvent:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"clickables must have a stable key. Error occurred on $this")

object OnChangeEventHandler:
  trait CanHandleOnChangeEvent:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"changeable must have a stable key. Error occurred on $this")

object OnChangeBooleanEventHandler:
  trait CanHandleOnChangeEvent:
    this: UiElement =>
    if key.isEmpty then throw new IllegalStateException(s"changeable must have a stable key. Error occurred on $this")
