package org.terminal21.client.model

import org.terminal21.client.{ControllerClickEvent, HandledEvent}
import org.terminal21.client.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.client.components.UiElement

case class OnClickController[M](
    element: UiElement & CanHandleOnClickEvent[_],
    handler: ControllerClickEvent[M] => HandledEvent[M]
)
