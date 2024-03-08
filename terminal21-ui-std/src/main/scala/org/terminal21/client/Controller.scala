package org.terminal21.client

import org.terminal21.client.Events.InitialRender
import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.{OnChangeBooleanEventHandler, OnChangeEventHandler, UiElement}
import org.terminal21.client.components.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

/** The initial function passed on to a controller in order to create the MVC iterator.
  * @tparam M
  *   the type of the model
  */
type ModelViewFunction[M] = (M, Events) => MV[M]

/** Controller manages the changes in the model by receiving events. Also the rendering of the view (which is UiElements).
  *
  * @tparam M
  *   the type of the model
  */
class Controller[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    modelViewFunction: ModelViewFunction[M]
):
  /** Sends the initialModel along with an InitialRender event to the modelViewFunction and renders the resulting UI.
    * @param initialModel
    *   the initial state of the model
    * @return
    *   a RenderedController. Call run() or iterator on that.
    */
  def render(initialModel: M): RenderedController[M] =
    val mv = modelViewFunction(initialModel, Events.Empty)
    renderChanges(mv.view)
    new RenderedController(eventIteratorFactory, renderChanges, modelViewFunction, mv)

trait NoModelController:
  this: Controller[Unit] =>
  def render(): RenderedController[Unit] = render(())

object Controller:
  /** Call this for a full blown model-view-controller
    * @param modelViewFunction
    *   a function (M, Events) => MV[M] which should process the events and render Seq[UiElement]
    * @param session
    *   the ConnectedSession
    * @tparam M
    *   the type of the model
    * @return
    *   Controller[M], call render(initialModel) and then iterator or run()
    */
  def apply[M](modelViewFunction: ModelViewFunction[M])(using session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.renderChanges, modelViewFunction)

  /** Call this id you just want to render some information UI that won't receive events.
    * @param component
    *   a single component (and it's children) to be rendered
    * @param session
    *   ConnectedSession
    * @return
    *   the controller.
    */
  def noModel(component: UiElement)(using session: ConnectedSession): Controller[Unit] with NoModelController = noModel(Seq(component))

  /** Call this id you just want to render some information UI that won't receive events.
    * @param components
    *   components to be rendered
    * @param session
    *   ConnectedSession
    * @return
    *   the controller.
    */
  def noModel(components: Seq[UiElement])(using session: ConnectedSession): Controller[Unit] with NoModelController =
    new Controller[Unit](session.eventIterator, session.renderChanges, (_, _) => MV((), components)) with NoModelController

  /** Call this if you have no model but still want to receive events. I.e. a form with just an "Ok" button
    *
    * @param materializer
    *   a function that will be called initially to render the UI and whenever there is an event to render any changes to the UI
    * @param session
    *   ConnectedSession
    * @return
    *   the controller.
    */
  def noModel(materializer: Events => Seq[UiElement])(using session: ConnectedSession): Controller[Unit] with NoModelController =
    new Controller[Unit](session.eventIterator, session.renderChanges, (_, events) => MV((), materializer(events))) with NoModelController

class RenderedController[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    materializer: ModelViewFunction[M],
    initialMv: MV[M]
):
  /** @return
    *   A new event iterator. There can be many event iterators on the same time and each of them iterates events only from after the time it was created. The
    *   iterator blocks while waiting to receive an event.
    *
    * Normally a single iterator is required and most of the time it is better done by the #run() method below.
    */
  def iterator: EventIterator[MV[M]] = new EventIterator[MV[M]](
    eventIteratorFactory
      .takeWhile(!_.isSessionClosed)
      .scanLeft(initialMv): (mv, e) =>
        val events = Events(e)
        val newMv  = materializer(mv.model, events)
        if mv.view != newMv.view then renderChanges(newMv.view)
        newMv
      .flatMap: mv =>
        // make sure we read the last MV change when terminating
        if mv.terminate then Seq(mv.copy(terminate = false), mv) else Seq(mv)
      .takeWhile(!_.terminate)
  )

  /** Gets an iterator and run the event processing.
    * @return
    *   The last value of the model or None if the user closed the session.
    */
  def run(): Option[M] = iterator.lastOption.map(_.model)

/** Wraps an event and has useful methods to process it.
  * @param event
  *   CommandEvent (like clicks, changed values, ClientEvent etc)
  */
case class Events(event: CommandEvent):
  def isClicked(e: UiElement): Boolean = event match
    case OnClick(key) => key == e.key
    case _            => false

  /** If an element is clicked this results in Some(value), otherwise None
    * @param e
    *   the element
    * @param value
    *   the value
    * @tparam V
    *   value type
    * @return
    *   Some(value) if e is clicked, None if not
    */
  def ifClicked[V](e: UiElement & CanHandleOnClickEvent, value: => V): Option[V] = if isClicked(e) then Some(value) else None

  /** @param e
    *   an editable element (like input)
    * @param default
    *   the default value
    * @return
    *   the new value of the editable (as received by an OnChange event) or the default value if the element's value didn't change
    */
  def changedValue(e: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent, default: String): String = changedValue(e).getOrElse(default)

  /** @param e
    *   an editable element (like input) that can receive OnChange events
    * @return
    *   Some(newValue) if the element received an OnChange event, None if not
    */
  def changedValue(e: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent): Option[String] = event match
    case OnChange(key, value) if key == e.key => Some(value)
    case _                                    => None

  /** @param e
    *   an editable element (like input)
    * @return
    *   true if the value of the element has changed, false if not
    */
  def isChangedValue(e: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent): Boolean =
    event match
      case OnChange(key, _) => key == e.key
      case _                => false

  /** @param e
    *   an editable element with boolean value (like checkbox)
    * @param default
    *   the value to return if the element wasn't changed
    * @return
    *   the element's changed value or the default if the element didn't change
    */
  def changedBooleanValue(e: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent, default: Boolean): Boolean =
    changedBooleanValue(e).getOrElse(default)

  /** @param e
    *   an editable element with boolean value (like checkbox)
    * @return
    *   Some(value) if the element changed or None if not
    */
  def changedBooleanValue(e: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent): Option[Boolean] = event match
    case OnChange(key, value) if key == e.key => Some(value.toBoolean)
    case _                                    => None

  /** @param e
    *   an editable element with boolean value (like checkbox)
    * @return
    *   true if the element changed or false if not
    */
  def isChangedBooleanValue(e: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent): Boolean =
    event match
      case OnChange(key, _) => key == e.key
      case _                => false

  def isInitialRender: Boolean = event == InitialRender

object Events:
  case object InitialRender extends ClientEvent

  val Empty = Events(InitialRender)

/** The ModelViewFunction should return this, which contains the changes to the model, the changed view and if the event iteration should terminate.
  * @param model
  *   the value of the model after processing the event
  * @param view
  *   the value of the view after processing the event
  * @param terminate
  *   if true, the event iteration will terminate
  * @tparam M
  *   the type of the model
  */
case class MV[M](model: M, view: Seq[UiElement], terminate: Boolean = false)

object MV:
  def apply[M](model: M, view: UiElement): MV[M] = MV(model, Seq(view))
