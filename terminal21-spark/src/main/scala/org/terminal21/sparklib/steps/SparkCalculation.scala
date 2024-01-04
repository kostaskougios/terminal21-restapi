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
