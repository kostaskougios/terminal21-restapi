package org.terminal21.client

import org.terminal21.client.collections.EventIterator
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.Box
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

type ModelViewMaterialized[M] = (M, Events) => MV[M]

class Controller[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    materializer: ModelViewMaterialized[M]
):
  def render(initialModel: M): RenderedController[M] =
    val mv = materializer(initialModel, Events.Empty)
    renderChanges(Seq(mv.view))
    new RenderedController(eventIteratorFactory, renderChanges, materializer, mv)

object Controller:
  def apply[M](materializer: ModelViewMaterialized[M])(using session: ConnectedSession): Controller[M] =
    new Controller(session.eventIterator, session.renderChanges, materializer)

  def noModel(components: Seq[UiElement])(using session: ConnectedSession) =
    apply((Unit, Events) => MV((), Box().withChildren(components*)))

class RenderedController[M](
    eventIteratorFactory: => Iterator[CommandEvent],
    renderChanges: Seq[UiElement] => Unit,
    materializer: ModelViewMaterialized[M],
    initialMv: MV[M]
):
  def iterator: EventIterator[MV[M]] = new EventIterator[MV[M]](
    eventIteratorFactory
      .takeWhile(_.isSessionClosed)
      .scanLeft(initialMv): (mv, e) =>
        val events = Events(e)
        val newMv  = materializer(mv.model, events)
        renderChanges(Seq(newMv.view))
        newMv
  )

case class Events(event: CommandEvent):
  def isClicked(e: UiElement): Boolean = event match
    case OnClick(key) => key == e.key
    case _            => false

  def changedValue(e: UiElement): Option[String] = event match
    case OnChange(key, value) if key == e.key => Some(value)
    case _                                    => None

object Events:
  case object InitialRender extends ClientEvent

  val Empty = Events(InitialRender)

case class MV[M](model: M, view: UiElement)
