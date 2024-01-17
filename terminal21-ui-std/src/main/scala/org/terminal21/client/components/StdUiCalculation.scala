package org.terminal21.client.components

import functions.fibers.FiberExecutor
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.chakra.*

import java.util.concurrent.atomic.AtomicBoolean

/** Creates a standard UI for a calculation which may take time. While the calculation runs, the UI is grayed out, including the dataUi component. When the
  * calculation completes, it allows for updating the dataUi component.
  * @tparam OUT
  *   the return value of the calculation.
  */
trait StdUiCalculation[OUT](
    name: String,
    dataUi: UiElement with HasStyle[_]
)(using session: ConnectedSession, executor: FiberExecutor)
    extends Calculation[OUT]
    with UiComponent:
  private val running = new AtomicBoolean(false)
  val badge           = Badge()
  val recalc          = Button(text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon())).onClick: () =>
    if running.compareAndSet(false, true) then
      try
        reCalculate()
      finally running.set(false)

  override def rendered: Seq[UiElement] =
    val header = Box(
      bg = "green",
      p = 4,
      children = Seq(
        HStack(children = Seq(Text(text = name), badge, recalc))
      )
    )
    Seq(header, dataUi)

  override def onError(t: Throwable): Unit =
    badge.text = s"Error: ${t.getMessage}"
    badge.colorScheme = Some("red")
    recalc.isDisabled = None
    session.renderChanges(badge, dataUi, recalc)
    super.onError(t)

  override protected def whenResultsNotReady(): Unit =
    badge.text = "Calculating"
    badge.colorScheme = Some("purple")
    recalc.isDisabled = Some(true)
    val newDataUi = dataUi.style(dataUi.style + ("filter" -> "grayscale(100%)"))
    session.renderChanges(badge, newDataUi, recalc)
    super.whenResultsNotReady()

  override protected def whenResultsReady(results: OUT): Unit =
    badge.text = "Ready"
    badge.colorScheme = None
    recalc.isDisabled = Some(false)
    val newDataUi = dataUi.style(dataUi.style - "filter")
    session.renderChanges(badge, newDataUi, recalc)
