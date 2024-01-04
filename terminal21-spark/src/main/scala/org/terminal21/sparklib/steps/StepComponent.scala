package org.terminal21.sparklib.steps

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.chakra.{Badge, Box}
import org.terminal21.client.components.{Keys, UiComponent, UiElement}

abstract class StepComponent(val key: String = Keys.nextKey, @volatile var children: Seq[UiElement]) extends UiComponent:
  def calculating(): Unit
  def ready(): Unit

object StepComponent:
  def stdStep(name: String, dataUi: UiElement)(using session: ConnectedSession) =
    val badge = Badge()

    new StepComponent(
      children = Seq(
        Box(text = name, bg = "green", p = 4),
        badge,
        dataUi
      )
    ):
      override def calculating(): Unit =
        badge.text = "Calculating"
        session.render()
      override def ready(): Unit       =
        badge.text = "Ready"
        session.render()
