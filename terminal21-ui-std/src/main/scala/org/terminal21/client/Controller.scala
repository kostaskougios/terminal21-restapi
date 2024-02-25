package org.terminal21.client

import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.client.components.UiElement.HasEventHandler
import org.terminal21.client.components.{OnChangeBooleanEventHandler, OnChangeEventHandler, OnClickEventHandler, UiElement}
import org.terminal21.collections.TypedMapKey
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

class Controller[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    fireEvent: CommandEvent => Unit,
    renderChanges: Seq[UiElement] => Unit,
    initialComponents: Seq[UiElement],
    initialModel: Model[M],
    eventHandlers: Seq[PartialFunction[ControllerEvent[M], HandledEvent[M]]]
):
  def render()(using session: ConnectedSession): this.type =
    session.render(initialComponents)
    this

  def onEvent(handler: PartialFunction[ControllerEvent[M], HandledEvent[M]]) =
    new Controller(
      eventIteratorFactory,
      fireEvent,
      renderChanges,
      initialComponents,
      initialModel,
      eventHandlers :+ handler
    )

  def lastEventOption: Option[M]       = eventsIterator.lastOption
  def eventsIterator: EventIterator[M] = new EventIterator(handledEventsIterator.takeWhile(!_.shouldTerminate).map(_.model))

  private def clickHandlersMap(h: HandledEvent[M]): Map[String, Seq[OnClickEventHandlerFunction[M]]]                 =
    h.componentsByKey.values
      .collect:
        case e: OnClickEventHandler.CanHandleOnClickEvent if e.dataStore.contains(initialModel.ClickKey) => (e.key, e.dataStore(initialModel.ClickKey))
      .toMap
  private def changeHandlersMap(h: HandledEvent[M]): Map[String, Seq[OnChangeEventHandlerFunction[M]]]               =
    h.componentsByKey.values
      .collect:
        case e: OnChangeEventHandler.CanHandleOnChangeEvent if e.dataStore.contains(initialModel.ChangeKey) => (e.key, e.dataStore(initialModel.ChangeKey))
      .toMap
  private def changeBooleanHandlersMap(h: HandledEvent[M]): Map[String, Seq[OnChangeBooleanEventHandlerFunction[M]]] =
    h.componentsByKey.values
      .collect:
        case e: OnChangeBooleanEventHandler.CanHandleOnChangeEvent if e.dataStore.contains(initialModel.ChangeBooleanKey) =>
          (e.key, e.dataStore(initialModel.ChangeBooleanKey))
      .toMap

  private def initialComponentsByKeyMap: Map[String, UiElement] =
    initialComponents
      .flatMap(_.flat)
      .map(c => (c.key, c))
      .toMap
      .withDefault(key => throw new IllegalArgumentException(s"Component with key=$key is not available"))

  private def updateComponentsFromEvent(handled: HandledEvent[M], event: CommandEvent): HandledEvent[M] =
    event match
      case _: ClientEvent => handled
      case _              =>
        handled.componentsByKey(event.key) match
          case e: UiElement with HasEventHandler =>
            event match
              case OnChange(key, value) =>
                handled.copy(componentsByKey = handled.componentsByKey + (key -> e.defaultEventHandler(value)))
              case _                    => handled
          case _                                 => handled

  private def invokeEventHandlers(initHandled: HandledEvent[M], event: CommandEvent): HandledEvent[M] =
    eventHandlers.foldLeft(initHandled.copy(renderChanges = Nil, timedRenderChanges = Nil)): (h, f) =>
      event match
        case OnClick(key)         =>
          val e = ControllerClickEvent(h.componentsByKey(key), h)
          if f.isDefinedAt(e) then f(e) else h
        case OnChange(key, value) =>
          val receivedBy = h.componentsByKey(key)
          val e          = receivedBy match
            case _: OnChangeEventHandler.CanHandleOnChangeEvent        => ControllerChangeEvent(receivedBy, h, value)
            case _: OnChangeBooleanEventHandler.CanHandleOnChangeEvent => ControllerChangeBooleanEvent(receivedBy, h, value.toBoolean)
          if f.isDefinedAt(e) then f(e) else h
        case ce: ClientEvent      =>
          val e = ControllerClientEvent(h, ce)
          if f.isDefinedAt(e) then f(e) else h
        case x                    => throw new IllegalStateException(s"Unexpected state $x")

  private def invokeComponentEventHandlers(h: HandledEvent[M], event: CommandEvent) =
    lazy val clickHandlers         = clickHandlersMap(h)
    lazy val changeHandlers        = changeHandlersMap(h)
    lazy val changeBooleanHandlers = changeBooleanHandlersMap(h)
    event match
      case OnClick(key) if clickHandlers.contains(key)                 =>
        val handlers   = clickHandlers(key)
        val receivedBy = h.componentsByKey(key)
        val handled    = handlers.foldLeft(h): (handled, handler) =>
          handler(ControllerClickEvent(receivedBy, handled))
        handled
      case OnChange(key, value) if changeHandlers.contains(key)        =>
        val handlers   = changeHandlers(key)
        val receivedBy = h.componentsByKey(key)
        val handled    = handlers.foldLeft(h): (handled, handler) =>
          handler(ControllerChangeEvent(receivedBy, handled, value))
        handled
      case OnChange(key, value) if changeBooleanHandlers.contains(key) =>
        val handlers   = changeBooleanHandlers(key)
        val receivedBy = h.componentsByKey(key)
        val handled    = handlers.foldLeft(h): (handled, handler) =>
          handler(ControllerChangeBooleanEvent(receivedBy, handled, value.toBoolean))
        handled
      case _                                                           => h

  private def includeRendered(handled: HandledEvent[M]): HandledEvent[M] =
    val newComponentsByKey =
      (handled.renderChanges.flatMap(_.flat) ++ handled.timedRenderChanges.flatMap(_.renderChanges).flatMap(_.flat)).map(e => (e.key, e)).toMap
    handled.copy(componentsByKey = handled.componentsByKey ++ newComponentsByKey)

  def handledEventsIterator: EventIterator[HandledEvent[M]] =
    new EventIterator(
      eventIteratorFactory
        .takeWhile(!_.isSessionClosed)
        .scanLeft(HandledEvent(initialModel.value, initialComponentsByKeyMap, Nil, Nil, false)): (oldHandled, event) =>
          val handled1 = includeRendered(updateComponentsFromEvent(oldHandled, event))
          val handled2 = includeRendered(invokeEventHandlers(handled1, event))
          val handled3 = includeRendered(invokeComponentEventHandlers(handled2, event))
          handled3
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
  def apply[M](initialModel: Model[M], components: Seq[UiElement])(using session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.fireEvent, session.renderChanges, components, initialModel, Nil)
  def apply[M](components: Seq[UiElement])(using initialModel: Model[M], session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.fireEvent, session.renderChanges, components, initialModel, Nil)

sealed trait ControllerEvent[M]:
  def model: M                                    = handled.model
  def handled: HandledEvent[M]
  extension [A <: UiElement](e: A) def current: A = handled.current(e)

case class ControllerClickEvent[M](clicked: UiElement, handled: HandledEvent[M])                            extends ControllerEvent[M]
case class ControllerChangeEvent[M](changed: UiElement, handled: HandledEvent[M], newValue: String)         extends ControllerEvent[M]
case class ControllerChangeBooleanEvent[M](changed: UiElement, handled: HandledEvent[M], newValue: Boolean) extends ControllerEvent[M]
case class ControllerClientEvent[M](handled: HandledEvent[M], event: ClientEvent)                           extends ControllerEvent[M]

case class HandledEvent[M](
    model: M,
    componentsByKey: Map[String, UiElement],
    renderChanges: Seq[UiElement],
    timedRenderChanges: Seq[TimedRenderChanges],
    shouldTerminate: Boolean
):
  def terminate: HandledEvent[M]                                                      = copy(shouldTerminate = true)
  def withShouldTerminate(t: Boolean): HandledEvent[M]                                = copy(shouldTerminate = t)
  def withModel(m: M): HandledEvent[M]                                                = copy(model = m)
  def withRenderChanges(changed: UiElement*): HandledEvent[M]                         = copy(renderChanges = renderChanges ++ changed)
  def withTimedRenderChanges(changed: TimedRenderChanges*): HandledEvent[M]           = copy(timedRenderChanges = changed)
  def addTimedRenderChange(waitInMs: Long, renderChanges: UiElement): HandledEvent[M] =
    copy(timedRenderChanges = timedRenderChanges :+ TimedRenderChanges(waitInMs, renderChanges))
  def current[A <: UiElement](e: A): A                                                = componentsByKey(e.key).asInstanceOf[A]

type OnClickEventHandlerFunction[M]         = ControllerClickEvent[M] => HandledEvent[M]
type OnChangeEventHandlerFunction[M]        = ControllerChangeEvent[M] => HandledEvent[M]
type OnChangeBooleanEventHandlerFunction[M] = ControllerChangeBooleanEvent[M] => HandledEvent[M]

case class TimedRenderChanges(waitInMs: Long, renderChanges: Seq[UiElement])
object TimedRenderChanges:
  def apply(waitInMs: Long, renderChanges: UiElement): TimedRenderChanges = TimedRenderChanges(waitInMs, Seq(renderChanges))

case class Model[M](value: M):
  object ClickKey         extends TypedMapKey[Seq[OnClickEventHandlerFunction[M]]]
  object ChangeKey        extends TypedMapKey[Seq[OnChangeEventHandlerFunction[M]]]
  object ChangeBooleanKey extends TypedMapKey[Seq[OnChangeBooleanEventHandlerFunction[M]]]

object Model:
  given unitModel: Model[Unit]            = Model(())
  given booleanFalseModel: Model[Boolean] = Model(false)
  given booleanTrueModel: Model[Boolean]  = Model(true)
