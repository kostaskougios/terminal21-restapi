package org.terminal21.sparklib.steps

import functions.fibers.FiberExecutor
import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.{Calculation, ConnectedSession}
import org.terminal21.client.components.chakra.{Badge, Box, Button, HStack, RepeatIcon, Text}
import org.terminal21.client.components.{Keys, UiComponent, UiElement}

abstract class SparkCalculation[IN, OUT](
    val key: String = Keys.nextKey,
    @volatile var children: Seq[UiElement],
    notifyWhenCalcReady: Seq[Calculation[OUT, _]]
)(using executor: FiberExecutor)
    extends Calculation[IN, OUT](notifyWhenCalcReady)
    with UiComponent

abstract class StdSparkCalculation[IN, OUT](
    key: String = Keys.nextKey,
    name: String,
    dataUi: UiElement with HasStyle,
    notifyWhenCalcReady: Seq[Calculation[OUT, _]]
)(using session: ConnectedSession, executor: FiberExecutor)
    extends SparkCalculation[IN, OUT](key, Nil, notifyWhenCalcReady):
  val badge  = Badge()
  val recalc = Button(text = "Recalculate", size = Some("sm"), leftIcon = Some(RepeatIcon())).onClick: () =>
    for i <- in do run(i)

  children = Seq(
    Box(bg = "green", p = 4).withChildren(
      HStack().withChildren(
        Text(text = name),
        badge,
        recalc
      )
    ),
    dataUi
  )

  private var in: Option[IN] = None
  override def run(in: IN)   =
    this.in = Some(in)
    super.run(in)

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

object SparkCalculation:
  class Builder[IN, OUT](name: String, dataUi: UiElement with HasStyle, notifyWhenCalcReady: Seq[Calculation[OUT, _]], calc: IN => OUT)(using
      session: ConnectedSession,
      executor: FiberExecutor
  ):
    def whenResultsReady(ready: OUT => Unit) =
      new StdSparkCalculation[IN, OUT](name = name, dataUi = dataUi, notifyWhenCalcReady):
        override protected def calculation(in: IN): OUT             = calc(in)
        override protected def whenResultsReady(results: OUT): Unit =
          ready(results)
          super.whenResultsReady(results)

  def stdSparkCalculation[IN, OUT](name: String, dataUi: UiElement with HasStyle, notifyWhenCalcReady: Calculation[OUT, _]*)(calc: IN => OUT)(using
      session: ConnectedSession,
      executor: FiberExecutor
  ) =
    new Builder[IN, OUT](name, dataUi, notifyWhenCalcReady, calc)
