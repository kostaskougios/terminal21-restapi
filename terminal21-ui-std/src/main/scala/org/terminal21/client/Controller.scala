package org.terminal21.client

import org.terminal21.client.Events.InitialRender
import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.{OnChangeBooleanEventHandler, OnChangeEventHandler, UiElement}
import org.terminal21.client.components.OnClickEventHandler.CanHandleOnClickEvent
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

type ModelViewMaterialized[M] = (M, Events) => MV[M]

class Controller[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    materializer: ModelViewMaterialized[M]
):
  def render(initialModel: M): RenderedController[M] =
    val mv = materializer(initialModel, Events.Empty)
    renderChanges(mv.view)
    new RenderedController(eventIteratorFactory, renderChanges, materializer, mv)

trait NoModelController:
  this: Controller[Unit] =>
  def render(): RenderedController[Unit] = render(())

object Controller:
  def apply[M](materializer: ModelViewMaterialized[M])(using session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.renderChanges, materializer)

  def noModel(component: UiElement)(using session: ConnectedSession): Controller[Unit] with NoModelController       = noModel(Seq(component))
  def noModel(components: Seq[UiElement])(using session: ConnectedSession): Controller[Unit] with NoModelController =
    new Controller[Unit](session.eventIterator, session.renderChanges, (_, _) => MV((), components)) with NoModelController

  def noModel(materializer: Events => Seq[UiElement])(using session: ConnectedSession) =
    new Controller[Unit](session.eventIterator, session.renderChanges, (_, events) => MV((), materializer(events))) with NoModelController

class RenderedController[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    materializer: ModelViewMaterialized[M],
    initialMv: MV[M]
):
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

  def run(): Option[M] = iterator.lastOption.map(_.model)

case class Events(event: CommandEvent):
  def isClicked(e: UiElement): Boolean = event match
    case OnClick(key) => key == e.key
    case _            => false

  def ifClicked[V](e: UiElement & CanHandleOnClickEvent, value: => V): Option[V] = if isClicked(e) then Some(value) else None

  def changedValue(e: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent, default: String): String = changedValue(e).getOrElse(default)
  def changedValue(e: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent): Option[String]          = event match
    case OnChange(key, value) if key == e.key => Some(value)
    case _                                    => None
  def isChangedValue(e: UiElement & OnChangeEventHandler.CanHandleOnChangeEvent): Boolean               =
    event match
      case OnChange(key, _) => key == e.key
      case _                => false

  def changedBooleanValue(e: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent, default: Boolean): Boolean =
    changedBooleanValue(e).getOrElse(default)
  def changedBooleanValue(e: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent): Option[Boolean]           = event match
    case OnChange(key, value) if key == e.key => Some(value.toBoolean)
    case _                                    => None
  def isChangedBooleanValue(e: UiElement & OnChangeBooleanEventHandler.CanHandleOnChangeEvent): Boolean                 =
    event match
      case OnChange(key, _) => key == e.key
      case _                => false

  def isInitialRender: Boolean = event == InitialRender

object Events:
  case object InitialRender extends ClientEvent

  val Empty = Events(InitialRender)

case class MV[M](model: M, view: Seq[UiElement], terminate: Boolean = false)

object MV:
  def apply[M](model: M, view: UiElement): MV[M] = MV(model, Seq(view))
