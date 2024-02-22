package org.terminal21.client

import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.{OnChangeBooleanEventHandler, OnChangeEventHandler, UiElement}
import org.terminal21.client.model.{GlobalEvent, UiEvent}
import org.terminal21.model.{OnChange, OnClick}

class Controller[M](
    eventIteratorFactory: => Iterator[GlobalEvent],
    renderChanges: Seq[UiElement] => Unit,
    initialModel: M,
    eventHandlers: Seq[ControllerEvent[M] => HandledEvent[M]],
    clickHandlers: Map[String, ControllerClickEvent[M] => HandledEvent[M]],
    changeHandlers: Map[String, ControllerChangeEvent[M] => HandledEvent[M]],
    changeBooleanHandlers: Map[String, ControllerChangeBooleanEvent[M] => HandledEvent[M]]
):
  def onEvent(handler: ControllerEvent[M] => HandledEvent[M]) =
    new Controller(eventIteratorFactory, renderChanges, initialModel, eventHandlers :+ handler, clickHandlers, changeHandlers, changeBooleanHandlers)

  def onClick(element: UiElement & CanHandleOnClickEvent[_])(handler: ControllerClickEvent[M] => HandledEvent[M]): Controller[M] = onClicked(element)(handler)
  def onClicked(elements: UiElement & CanHandleOnClickEvent[_]*)(handler: ControllerClickEvent[M] => HandledEvent[M]): Controller[M] =
    new Controller(
      eventIteratorFactory,
      renderChanges,
      initialModel,
      eventHandlers,
      clickHandlers ++ elements.map(e => e.key -> handler),
      changeHandlers,
      changeBooleanHandlers
    )

  def onChange(element: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent[_])(handler: ControllerChangeEvent[M] => HandledEvent[M]): Controller[M]    =
    onChanged(element)(handler)
  def onChanged(elements: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent[_]*)(handler: ControllerChangeEvent[M] => HandledEvent[M]): Controller[M] =
    new Controller(
      eventIteratorFactory,
      renderChanges,
      initialModel,
      eventHandlers,
      clickHandlers,
      changeHandlers ++ elements.map(e => e.key -> handler),
      changeBooleanHandlers
    )

  def onChange(element: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent[_])(
      handler: ControllerChangeBooleanEvent[M] => HandledEvent[M]
  ): Controller[M] =
    onChangedBoolean(element)(handler)

  def onChangedBoolean(
      elements: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent[_]*
  )(handler: ControllerChangeBooleanEvent[M] => HandledEvent[M]): Controller[M] =
    new Controller(
      eventIteratorFactory,
      renderChanges,
      initialModel,
      eventHandlers,
      clickHandlers,
      changeHandlers,
      changeBooleanHandlers ++ elements.map(e => e.key -> handler)
    )

  def eventsIterator: EventIterator[M] = new EventIterator(handledEventsIterator.takeWhile(!_.shouldTerminate).map(_.model))

  def handledEventsIterator: EventIterator[HandledEvent[M]] =
    new EventIterator(
      eventIteratorFactory
        .takeWhile(!_.isSessionClose)
        .scanLeft(HandledEvent(initialModel, Nil, Nil, false)): (oldHandled, event) =>
          val h = eventHandlers.foldLeft(oldHandled): (h, f) =>
            event match
              case UiEvent(OnClick(_), receivedBy)         =>
                f(ControllerClickEvent(receivedBy, h.model))
              case UiEvent(OnChange(_, value), receivedBy) =>
                val e = receivedBy match
                  case _: OnChangeEventHandler.CanHandleOnChangeEvent[_]        => ControllerChangeEvent(receivedBy, h.model, value)
                  case _: OnChangeBooleanEventHandler.CanHandleOnChangeEvent[_] => ControllerChangeBooleanEvent(receivedBy, h.model, value.toBoolean)
                f(e)
              case x                                       => throw new IllegalStateException(s"Unexpected state $x")

          val handled = event match
            case UiEvent(OnClick(key), receivedBy) if clickHandlers.contains(key)                 =>
              val handler = clickHandlers(key)
              val handled = handler(ControllerClickEvent(receivedBy, h.model))
              handled
            case UiEvent(OnChange(key, value), receivedBy) if changeHandlers.contains(key)        =>
              val handler = changeHandlers(key)
              val handled = handler(ControllerChangeEvent(receivedBy, h.model, value))
              handled
            case UiEvent(OnChange(key, value), receivedBy) if changeBooleanHandlers.contains(key) =>
              val handler = changeBooleanHandlers(key)
              val handled = handler(ControllerChangeBooleanEvent(receivedBy, h.model, value.toBoolean))
              handled
            case _                                                                                => h
          handled
        .tapEach: handled =>
          renderChanges(handled.renderChanges)
          for trc <- handled.timedRenderChanges do
            fiberExecutor.submit:
              Thread.sleep(trc.waitInMs)
              renderChanges(trc.renderChanges)
        .flatMap: h =>
          // trick to make sure we take the last state of the model when shouldTerminate=true
          if h.shouldTerminate then Seq(h.copy(shouldTerminate = false), h) else Seq(h)
    )

object Controller:
  def apply[M](initialModel: M)(using session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.renderChanges, initialModel, Nil, Map.empty, Map.empty, Map.empty)

trait ControllerEvent[M]:
  def model: M
  def handled: HandledEvent[M] = HandledEvent(model, Nil, Nil, false)

case class ControllerClickEvent[M](clicked: UiElement, model: M)                            extends ControllerEvent[M]
case class ControllerChangeEvent[M](changed: UiElement, model: M, newValue: String)         extends ControllerEvent[M]
case class ControllerChangeBooleanEvent[M](changed: UiElement, model: M, newValue: Boolean) extends ControllerEvent[M]

case class HandledEvent[M](model: M, renderChanges: Seq[UiElement], timedRenderChanges: Seq[TimedRenderChanges], shouldTerminate: Boolean):
  def terminate: HandledEvent[M]                                                      = copy(shouldTerminate = true)
  def withShouldTerminate(t: Boolean): HandledEvent[M]                                = copy(shouldTerminate = t)
  def withModel(m: M): HandledEvent[M]                                                = copy(model = m)
  def withRenderChanges(changed: UiElement*): HandledEvent[M]                         = copy(renderChanges = changed)
  def withTimedRenderChanges(changed: TimedRenderChanges*): HandledEvent[M]           = copy(timedRenderChanges = changed)
  def addTimedRenderChange(waitInMs: Long, renderChanges: UiElement): HandledEvent[M] =
    copy(timedRenderChanges = timedRenderChanges :+ TimedRenderChanges(waitInMs, renderChanges))

case class TimedRenderChanges(waitInMs: Long, renderChanges: Seq[UiElement])
object TimedRenderChanges:
  def apply(waitInMs: Long, renderChanges: UiElement): TimedRenderChanges = TimedRenderChanges(waitInMs, Seq(renderChanges))
