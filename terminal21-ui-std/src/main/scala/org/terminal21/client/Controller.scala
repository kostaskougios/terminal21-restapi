package org.terminal21.client

import org.terminal21.client.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.client.components.UiElement
import org.terminal21.client.model.UiEvent
import org.terminal21.model.{OnChange, OnClick}

class Controller[M](
    session: ConnectedSession,
    initialModel: M,
    eventHandlers: Seq[M => M],
    clickHandlers: Map[String, ControllerClickEvent[M] => HandledEvent[M]],
    changeHandlers: Map[String, ControllerChangeEvent[M] => HandledEvent[M]]
):
  def onEvent(handler: M => M) =
    new Controller(session, initialModel, eventHandlers :+ handler, clickHandlers, changeHandlers)

  def onClick(elements: UiElement & CanHandleOnClickEvent[_]*)(handler: ControllerClickEvent[M] => HandledEvent[M]) =
    new Controller(session, initialModel, eventHandlers, clickHandlers ++ elements.map(e => e.key -> handler), changeHandlers)

  def onChange(elements: UiElement & CanHandleOnChangeEvent[_]*)(handler: ControllerChangeEvent[M] => HandledEvent[M]) =
    new Controller(session, initialModel, eventHandlers, clickHandlers, changeHandlers ++ elements.map(e => e.key -> handler))

  def iterator: Iterator[M] =
    session.eventIterator
      .takeWhile(!_.isSessionClose)
      .scanLeft(HandledEvent(initialModel, Nil, false)): (oldHandled, event) =>
        val newModel = eventHandlers.foldLeft(oldHandled.model): (model, f) =>
          f(model)

        val h       = oldHandled.copy(model = newModel)
        val handled = event match
          case UiEvent(OnClick(key), receivedBy) if clickHandlers.contains(key)          =>
            val handler = clickHandlers(key)
            val handled = handler(ControllerClickEvent(receivedBy, h.model))
            handled
          case UiEvent(OnChange(key, value), receivedBy) if changeHandlers.contains(key) =>
            val handler = changeHandlers(key)
            val handled = handler(ControllerChangeEvent(receivedBy, h.model, value))
            handled
          case _                                                                         => h
        session.renderChanges(handled.renderChanges: _*)
        handled
      .takeWhile(!_.shouldTerminate)
      .map(_.model)

  def lastModelOption: Option[M] = iterator.toList.lastOption

object Controller:
  def apply[M](initialModel: M)(using session: ConnectedSession) = new Controller(session, initialModel, Nil, Map.empty, Map.empty)

trait ControllerEvent[M]:
  def model: M
  def handled: HandledEvent[M] = HandledEvent(model, Nil, false)

case class ControllerClickEvent[M](clicked: UiElement, model: M)                    extends ControllerEvent[M]
case class ControllerChangeEvent[M](changed: UiElement, model: M, newValue: String) extends ControllerEvent[M]

case class HandledEvent[M](model: M, renderChanges: Seq[UiElement], shouldTerminate: Boolean):
  def terminate: HandledEvent[M]                              = copy(shouldTerminate = true)
  def withShouldTerminate(t: Boolean): HandledEvent[M]        = copy(shouldTerminate = t)
  def withModel(m: M): HandledEvent[M]                        = copy(model = m)
  def withRenderChanges(changed: UiElement*): HandledEvent[M] = copy(renderChanges = changed)
