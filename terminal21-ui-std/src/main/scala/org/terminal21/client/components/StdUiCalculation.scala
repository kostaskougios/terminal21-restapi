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
    dataUi: UiElement with HasStyle
)(using session: ConnectedSession, executor: FiberExecutor)
    extends Calculation[OUT]
    with UiComponent:
  val badge           = Badge()
  private val running = new AtomicBoolean(false)
  val recalc          = Button(text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon())).onClick: () =>
    if running.compareAndSet(false, true) then
      try
        reRunRequested()
      finally running.set(false)

  val header                             = Box(bg = "green", p = 4).withChildren(
    HStack().withChildren(
      Text(text = name),
      badge,
      recalc
    )
  )
  @volatile var children: Seq[UiElement] = Seq(
    header,
    dataUi
  )

  override protected def whenResultsNotReady(): Unit =
    badge.text = "Calculating"
    badge.colorScheme = Some("purple")
    recalc.isDisabled = Some(true)
    dataUi.style = dataUi.style + ("filter" -> "grayscale(100%)")
    session.render()
    super.whenResultsNotReady()

  override protected def whenResultsReady(results: OUT): Unit =
    badge.text = "Ready"
    badge.colorScheme = None
    recalc.isDisabled = Some(false)
    dataUi.style = dataUi.style - "filter"
    session.render()
