package org.terminal21.client

import org.slf4j.LoggerFactory
import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.OnChangeEventHandler.CanHandleOnChangeEvent
import org.terminal21.client.components.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.client.components.UiElement.HasChildren
import org.terminal21.client.components.{OnChangeBooleanEventHandler, OnChangeEventHandler, OnClickEventHandler, UiElement}
import org.terminal21.collections.{TMMap, TypedMap, TypedMapKey}
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

import scala.reflect.{ClassTag, classTag}

type EventHandler    = PartialFunction[ControllerEvent[_], Handled[_]]
type ComponentsByKey = Map[String, UiElement]

class Controller(
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    modelComponents: Seq[UiElement],
    initialModelValues: Map[Model[Any], Any],
    eventHandlers: Seq[EventHandler]
):

  private def applyModelTo(components: Seq[UiElement]): Seq[UiElement] =
    initialModelValues
      .flatMap: (m, v) =>
        components.map: e =>
          val ne = if e.hasModelChangeHandler(using m) then e.fireModelChange(using m)(v) else e
          ne match
            case ch: HasChildren => ch.withChildren(applyModelTo(ch.children)*)
            case x               => x
      .toList

  def render()(using session: ConnectedSession): RenderedController =
    val elements = applyModelTo(modelComponents)
    session.render(elements)
    new RenderedController(
      eventIteratorFactory,
      initialModelValues,
      elements,
      renderChanges,
      eventHandlers :+ renderChangesEventHandler
    )

  private def renderChangesEventHandler: PartialFunction[ControllerEvent[_], Handled[_]] =
    case ControllerClientEvent(h, RenderChangesEvent(changes)) =>
      h.copy(renderedChanges = h.renderedChanges ++ changes)

  def onEvent(handler: EventHandler) =
    new Controller(
      eventIteratorFactory,
      renderChanges,
      modelComponents,
      initialModelValues,
      eventHandlers :+ handler
    )

object Controller:
//  def apply[M](initialModel: Model[M], modelComponents: Seq[UiElement])(using session: ConnectedSession): Controller[M] =
//    new Controller(session.eventIterator, session.renderChanges, modelComponents, initialModel, Nil)
  def apply[M](initialValue: M, modelComponents: Seq[UiElement])(using initialModel: Model[M], session: ConnectedSession): Controller =
    new Controller(session.eventIterator, session.renderChanges, modelComponents, Map(initialModel.asInstanceOf[Model[Any]] -> initialValue), Nil)
  def noModel(modelComponents: Seq[UiElement])(using session: ConnectedSession): Controller                                           =
    apply((), modelComponents)(using Model.Standard.unitModel, session)

class RenderedController(
    eventIteratorFactory: => Iterator[CommandEvent],
    initialModelValues: Map[Model[Any], Any],
    initialComponents: Seq[UiElement],
    renderChanges: Seq[UiElement] => Unit,
    eventHandlers: Seq[EventHandler]
):
  private val logger = LoggerFactory.getLogger(getClass)

  private def invokeEventHandlers[A](initHandled: Handled[A], componentsByKey: ComponentsByKey, event: CommandEvent): Handled[A] =
    eventHandlers
      .foldLeft(initHandled): (h, f) =>
        event match
          case OnClick(key)         =>
            val e = ControllerClickEvent(componentsByKey(key), h)
            if f.isDefinedAt(e) then f(e).asInstanceOf[Handled[A]] else h
          case OnChange(key, value) =>
            val receivedBy = componentsByKey(key)
            val e          = receivedBy match
              case _: OnChangeEventHandler.CanHandleOnChangeEvent        => ControllerChangeEvent(receivedBy, h, value)
              case _: OnChangeBooleanEventHandler.CanHandleOnChangeEvent => ControllerChangeBooleanEvent(receivedBy, h, value.toBoolean)
            if f.isDefinedAt(e) then f(e).asInstanceOf[Handled[A]] else h
          case ce: ClientEvent      =>
            val e = ControllerClientEvent(h, ce)
            if f.isDefinedAt(e) then f(e).asInstanceOf[Handled[A]] else h
          case x                    => throw new IllegalStateException(s"Unexpected state $x")

  private def clickHandlersMap[A](allComponents: Seq[UiElement], h: Handled[A]): Map[String, Seq[OnClickEventHandlerFunction[A]]] =
    allComponents
      .collect:
        case e: OnClickEventHandler.CanHandleOnClickEvent if e.dataStore.contains(h.mm.ClickKey) => (e.key, e.dataStore(h.mm.ClickKey))
      .toMap

  private def changeHandlersMap[A](allComponents: Seq[UiElement], h: Handled[A]): Map[String, Seq[OnChangeEventHandlerFunction[A]]] =
    allComponents
      .collect:
        case e: OnChangeEventHandler.CanHandleOnChangeEvent if e.dataStore.contains(h.mm.ChangeKey) => (e.key, e.dataStore(h.mm.ChangeKey))
      .toMap

  private def changeBooleanHandlersMap[A](allComponents: Seq[UiElement], h: Handled[A]): Map[String, Seq[OnChangeBooleanEventHandlerFunction[A]]] =
    allComponents
      .collect:
        case e: OnChangeBooleanEventHandler.CanHandleOnChangeEvent if e.dataStore.contains(h.mm.ChangeBooleanKey) =>
          (e.key, e.dataStore(h.mm.ChangeBooleanKey))
      .toMap

  private def invokeComponentEventHandlers[A](h: Handled[A], componentsByKey: ComponentsByKey, event: CommandEvent): Handled[A] =
    val allComponents              = componentsByKey.values.toList
    lazy val clickHandlers         = clickHandlersMap(allComponents, h)
    lazy val changeHandlers        = changeHandlersMap(allComponents, h)
    lazy val changeBooleanHandlers = changeBooleanHandlersMap(allComponents, h)
    println(event.toString + "/" + h.mm)
    event match
      case OnClick(key) if clickHandlers.contains(key)                 =>
        val handlers   = clickHandlers(key)
        val receivedBy = componentsByKey(key)
        val handled    = handlers.foldLeft(h): (handled, handler) =>
          handler(ControllerClickEvent(receivedBy, handled))
        handled
      case OnChange(key, value) if changeHandlers.contains(key)        =>
        val handlers   = changeHandlers(key)
        val receivedBy = componentsByKey(key)
        val handled    = handlers.foldLeft(h): (handled, handler) =>
          handler(ControllerChangeEvent(receivedBy, handled, value))
        handled
      case OnChange(key, value) if changeBooleanHandlers.contains(key) =>
        val handlers   = changeBooleanHandlers(key)
        val receivedBy = componentsByKey(key)
        val handled    = handlers.foldLeft(h): (handled, handler) =>
          handler(ControllerChangeBooleanEvent(receivedBy, handled, value.toBoolean))
        handled
      case ModelChangeEvent(model, newValue) if model == h.mm          =>
        h.withModel(model, newValue)
      case _                                                           => h

  private def checkForDuplicatesAndThrow(components: Seq[UiElement]): Unit =
    val duplicates = components.map(_.key).groupBy(identity).filter(_._2.size > 1).keys.toSet
    if duplicates.nonEmpty then
      val duplicateComponents = components.filter(e => duplicates.contains(e.key))
      throw new IllegalArgumentException(s"Duplicate(s) found: ${duplicates.mkString(", ")}\nDuplicate components:\n${duplicateComponents.mkString("\n")}")

  private def calcComponentsByKeyMap(components: Seq[UiElement]): Map[String, UiElement] =
    val flattened = components
      .flatMap(_.flat)
    checkForDuplicatesAndThrow(flattened)
    val all       = flattened
      .map(c => (c.key, c))
      .toMap
    all.withDefault(key =>
      throw new IllegalArgumentException(
        s"Component with key=$key is not available. Here are all available components:\n${all.values.map(_.toSimpleString).mkString("\n")}"
      )
    )

  private def renderChangesWhenModelChanges[A](
      oldHandled: Handled[A],
      newHandled: Handled[A],
      componentsByKey: ComponentsByKey
  ): (ComponentsByKey, Handled[A]) =
    if oldHandled.modelOption == newHandled.modelOption then (componentsByKey, newHandled)
    else
      val changeFunctions =
        for
          e <- componentsByKey.values
          f <- e.dataStore.get(newHandled.mm.OnModelChangeKey)
        yield (e, f)

      val dsEmpty = TypedMap.empty
      val changed = changeFunctions
        .map: (e, f) =>
          (e, f(e, newHandled.model))
        .filter: (e, ne) =>
          e.withDataStore(dsEmpty) != ne.withDataStore(dsEmpty)
        .map(_._2)
        .toList
      (
        componentsByKey ++ calcComponentsByKeyMap(changed),
        newHandled.copy(renderedChanges = newHandled.renderedChanges ++ changed)
      )

  private def initialModelsMap: TypedMap =
    val m = initialModelValues.map: (k, v) =>
      (k.ModelKey, v)
    new TypedMap(m.asInstanceOf[TMMap])

  private def availableModels(componentsByKey: ComponentsByKey): Seq[Model[_]] =
    (initialModelValues.keys.toList ++ componentsByKey.values.flatMap(_.handledModels).toList).distinct

  def handledEventsIterator: EventIterator[HandledEvent] =
    val initCompByKeyMap    = calcComponentsByKeyMap(initialComponents)
    val initAvailableModels = availableModels(initCompByKeyMap)
    val initHandledEvent    = HandledEvent(initAvailableModels, initialModelsMap, initCompByKeyMap, false, Nil)
    new EventIterator(
      eventIteratorFactory
        .takeWhile(!_.isSessionClosed)
        .scanLeft(initHandledEvent):
          case (ohEvent, event) =>
            try
              ohEvent.models.foldLeft(ohEvent):
                case (oldHandledEvent, model) =>
                  val oldHandled                    = oldHandledEvent.toHandled(model).copy(renderedChanges = Nil)
                  val handled2                      = invokeEventHandlers(oldHandled, oldHandledEvent.componentsByKey, event)
                  val handled3                      = invokeComponentEventHandlers(handled2, oldHandledEvent.componentsByKey, event)
                  val (componentsByKey, newHandled) = renderChangesWhenModelChanges(oldHandled, handled3, oldHandledEvent.componentsByKey)
                  if newHandled.renderedChanges.nonEmpty then renderChanges(newHandled.renderedChanges)
                  oldHandledEvent.copy(
                    modelValues = newHandled.modelValues,
                    componentsByKey = componentsByKey,
                    shouldTerminate = newHandled.shouldTerminate,
                    renderedChanges = newHandled.renderedChanges
                  )
            catch
              case t: Throwable =>
                logger.error("an error occurred while iterating events", t)
                ohEvent
        .flatMap: h =>
          // trick to make sure we take the last state of the model when shouldTerminate=true
          if h.shouldTerminate then Seq(h.copy(shouldTerminate = false), h) else Seq(h)
        .takeWhile(!_.shouldTerminate)
    )

sealed trait ControllerEvent[M]:
  def model: M = handled.model
  def handled: Handled[M]

case class ControllerClickEvent[M](clicked: UiElement, handled: Handled[M]) extends ControllerEvent[M]

case class ControllerChangeEvent[M](changed: UiElement, handled: Handled[M], newValue: String) extends ControllerEvent[M]

case class ControllerChangeBooleanEvent[M](changed: UiElement, handled: Handled[M], newValue: Boolean) extends ControllerEvent[M]
case class ControllerClientEvent[M](handled: Handled[M], event: ClientEvent)                           extends ControllerEvent[M]

case class Handled[M](
    mm: Model[M],
    modelValues: TypedMap,
    shouldTerminate: Boolean,
    renderedChanges: Seq[UiElement]
):
  def model: M                                               = modelValues(mm.ModelKey)
  def modelOption: Option[M]                                 = modelValues.get(mm.ModelKey)
  def withModel(m: M): Handled[M]                            = copy(modelValues = modelValues + (mm.ModelKey -> m))
  def withModel[A](model: Model[A], newValue: A): Handled[M] = copy(modelValues = modelValues + (model.ModelKey -> newValue))
  def mapModel(f: M => M): Handled[M]                        = withModel(f(model))
  def terminate: Handled[M]                                  = copy(shouldTerminate = true)
  def withShouldTerminate(t: Boolean): Handled[M]            = copy(shouldTerminate = t)

case class HandledEvent(
    models: Seq[Model[_]],
    modelValues: TypedMap,
    componentsByKey: ComponentsByKey,
    shouldTerminate: Boolean,
    renderedChanges: Seq[UiElement]
):
  def model[A](using model: Model[A]): A        = modelOf(model)
  def modelOf[A](model: Model[A]): A            = modelValues(model.ModelKey)
  def toHandled[A](model: Model[A]): Handled[A] = Handled[A](model, modelValues, shouldTerminate, renderedChanges)

type OnClickEventHandlerFunction[M]         = ControllerClickEvent[M] => Handled[M]
type OnChangeEventHandlerFunction[M]        = ControllerChangeEvent[M] => Handled[M]
type OnChangeBooleanEventHandlerFunction[M] = ControllerChangeBooleanEvent[M] => Handled[M]

class Model[M: ClassTag](name: String):
  type OnModelChangeFunction = (UiElement, M) => UiElement
  object ModelKey         extends TypedMapKey[M]
  object OnModelChangeKey extends TypedMapKey[OnModelChangeFunction]
  object ClickKey         extends TypedMapKey[Seq[OnClickEventHandlerFunction[M]]]
  object ChangeKey        extends TypedMapKey[Seq[OnChangeEventHandlerFunction[M]]]
  object ChangeBooleanKey extends TypedMapKey[Seq[OnChangeBooleanEventHandlerFunction[M]]]
  override def toString = s"Model($name)"

object Model:
  def apply[M: ClassTag]: Model[M]               = new Model[M](classTag[M].runtimeClass.getName)
  def apply[M: ClassTag](name: String): Model[M] = new Model[M](name)
  object Standard:
    given unitModel: Model[Unit]       = Model[Unit]("unit")
    given booleanModel: Model[Boolean] = Model[Boolean]("boolean")

case class RenderChangesEvent(changes: Seq[UiElement])       extends ClientEvent
case class ModelChangeEvent[M](model: Model[M], newValue: M) extends ClientEvent
