package org.terminal21.sparklib.steps

import functions.fibers.FiberExecutor
import org.terminal21.client.{Calculation, ConnectedSession}
import org.terminal21.client.components.chakra.{Badge, Box}
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
    dataUi: UiElement,
    notifyWhenCalcReady: Seq[Calculation[OUT, _]]
)(using session: ConnectedSession, executor: FiberExecutor)
    extends SparkCalculation[IN, OUT](key, Nil, notifyWhenCalcReady):
  val badge = Badge()
  children = Seq(
    Box(text = name, bg = "green", p = 4),
    badge,
    dataUi
  )

  override protected def whenResultsNotReady(): Unit =
    badge.text = "Calculating"
    session.render()

  override protected def whenResultsReady(results: OUT): Unit =
    badge.text = "Ready"
    session.render()

object SparkCalculation:
  class Builder[IN, OUT](name: String, dataUi: UiElement, notifyWhenCalcReady: Seq[Calculation[OUT, _]], calc: IN => OUT)(using
      session: ConnectedSession,
      executor: FiberExecutor
  ):
    def whenResultsReady(ready: OUT => Unit) =
      new StdSparkCalculation[IN, OUT](name = name, dataUi = dataUi, notifyWhenCalcReady):
        override protected def calculation(in: IN): OUT             = calc(in)
        override protected def whenResultsReady(results: OUT): Unit =
          ready(results)
          super.whenResultsReady(results)

  def stdSparkCalculation[IN, OUT](name: String, dataUi: UiElement, notifyWhenCalcReady: Calculation[OUT, _]*)(calc: IN => OUT)(using
      session: ConnectedSession,
      executor: FiberExecutor
  ) =
    new Builder[IN, OUT](name, dataUi, notifyWhenCalcReady, calc)
