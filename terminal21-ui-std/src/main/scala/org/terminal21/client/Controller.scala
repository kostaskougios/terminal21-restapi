package org.terminal21.client

import org.slf4j.LoggerFactory
import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.client.components.{Keys, OnChangeBooleanEventHandler, OnChangeEventHandler, OnClickEventHandler, UiElement}
import org.terminal21.collections.TypedMapKey
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

class Controller[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    modelComponents: M => Seq[UiElement],
    initialModel: Model[M],
    eventHandlers: Seq[PartialFunction[ControllerEvent[M], HandledEvent[M]]]
):
  def render()(using session: ConnectedSession): RenderedController[M] =
    val initComponents = Keys.linearKeys(modelComponents(initialModel.value))
    session.render(initComponents)
    new RenderedController(eventIteratorFactory, initialModel, initComponents, modelComponents, renderChanges, eventHandlers)

  def onEvent(handler: PartialFunction[ControllerEvent[M], HandledEvent[M]]) =
    new Controller(
      eventIteratorFactory,
      renderChanges,
      modelComponents,
      initialModel,
      eventHandlers :+ handler
    )

class RenderedController[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    initialModel: Model[M],
    initialComponents: Seq[UiElement],
    modelComponents: M => Seq[UiElement],
    renderChanges: Seq[UiElement] => Unit,
    eventHandlers: Seq[PartialFunction[ControllerEvent[M], HandledEvent[M]]]
):
  private val logger                                                                                                 = LoggerFactory.getLogger(getClass)
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

  private def invokeEventHandlers(initHandled: HandledEvent[M], event: CommandEvent): HandledEvent[M] =
    eventHandlers.foldLeft(initHandled): (h, f) =>
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

  private def invokeComponentEventHandlers(h: HandledEvent[M], event: CommandEvent): HandledEvent[M] =
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

  private def checkForDuplicatesAndThrow(seq: Seq[String]): Unit =
    val duplicates = seq.groupBy(identity).filter(_._2.size > 1).keys.toList
    if duplicates.nonEmpty then throw new IllegalArgumentException(s"Duplicate(s) found: ${duplicates.mkString(", ")}")

  private def calcComponentsByKeyMap(components: Seq[UiElement]): Map[String, UiElement] =
    val flattened = components
      .flatMap(_.flat)
    checkForDuplicatesAndThrow(flattened.map(_.key))
    val all       = flattened
      .map(c => (c.key, c))
      .toMap
    all.withDefault(key =>
      throw new IllegalArgumentException(
        s"Component with key=$key is not available. Here are all available components:\n${all.values.map(_.toSimpleString).mkString("\n")}"
      )
    )

  private def doRenderChanges(oldHandled: HandledEvent[M], newHandled: HandledEvent[M]): HandledEvent[M] =
    // TODO: optimise what elements are rendered
    val all = Keys.linearKeys(modelComponents(newHandled.model))
    renderChanges(all)
    newHandled.copy(componentsByKey = calcComponentsByKeyMap(all))

  def handledEventsIterator: EventIterator[HandledEvent[M]] =
    val initHandled = HandledEvent(initialModel.value, calcComponentsByKeyMap(initialComponents), false)
    new EventIterator(
      eventIteratorFactory
        .takeWhile(!_.isSessionClosed)
        .scanLeft((initHandled, initHandled)):
          case ((_, oldHandled), event) =>
            try
              val handled2 = invokeEventHandlers(oldHandled, event)
              val handled3 = invokeComponentEventHandlers(handled2, event)
              (oldHandled, handled3)
            catch
              case t: Throwable =>
                logger.error("an error occurred while iterating events", t)
                (oldHandled, oldHandled)
        .map: (oldHandled, newHandled) =>
          if oldHandled.model != newHandled.model then doRenderChanges(oldHandled, newHandled) else newHandled
        .flatMap: h =>
          // trick to make sure we take the last state of the model when shouldTerminate=true
          if h.shouldTerminate then Seq(h.copy(shouldTerminate = false), h) else Seq(h)
        .takeWhile(!_.shouldTerminate)
    )

object Controller:
  def apply[M](initialModel: Model[M], modelComponents: M => Seq[UiElement])(using session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.renderChanges, modelComponents, initialModel, Nil)
  def apply[M](modelComponents: M => Seq[UiElement])(using initialModel: Model[M], session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.renderChanges, modelComponents, initialModel, Nil)

sealed trait ControllerEvent[M]:
  def model: M = handled.model
  def handled: HandledEvent[M]

case class ControllerClickEvent[M](clicked: UiElement, handled: HandledEvent[M])                            extends ControllerEvent[M]
case class ControllerChangeEvent[M](changed: UiElement, handled: HandledEvent[M], newValue: String)         extends ControllerEvent[M]
case class ControllerChangeBooleanEvent[M](changed: UiElement, handled: HandledEvent[M], newValue: Boolean) extends ControllerEvent[M]
case class ControllerClientEvent[M](handled: HandledEvent[M], event: ClientEvent)                           extends ControllerEvent[M]

case class HandledEvent[M](
    model: M,
    componentsByKey: Map[String, UiElement],
    shouldTerminate: Boolean
):
  def terminate: HandledEvent[M]                       = copy(shouldTerminate = true)
  def withShouldTerminate(t: Boolean): HandledEvent[M] = copy(shouldTerminate = t)
  def withModel(m: M): HandledEvent[M]                 = copy(model = m)

type OnClickEventHandlerFunction[M]         = ControllerClickEvent[M] => HandledEvent[M]
type OnChangeEventHandlerFunction[M]        = ControllerChangeEvent[M] => HandledEvent[M]
type OnChangeBooleanEventHandlerFunction[M] = ControllerChangeBooleanEvent[M] => HandledEvent[M]

case class Model[M](value: M):
  object ClickKey         extends TypedMapKey[Seq[OnClickEventHandlerFunction[M]]]
  object ChangeKey        extends TypedMapKey[Seq[OnChangeEventHandlerFunction[M]]]
  object ChangeBooleanKey extends TypedMapKey[Seq[OnChangeBooleanEventHandlerFunction[M]]]

object Model:
  object Standard:
    given unitModel: Model[Unit]            = Model(())
    given booleanFalseModel: Model[Boolean] = Model(false)
    given booleanTrueModel: Model[Boolean]  = Model(true)
