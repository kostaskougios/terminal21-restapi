package org.terminal21.sparklib.steps

import org.terminal21.client.{Calculation, ConnectedSession}
import org.terminal21.client.components.chakra.*
import org.terminal21.client.given

class StepsCalculation(session: ConnectedSession):
  def calc[IN, OUT](name: String)(renderer: OUT => Unit)(calc: IN => OUT): Calculation[IN, OUT] =
    val badge = Badge()
    val ui    = Seq(
      Box(text = name, bg = "green", p = 4),
      badge
    )
    session.add(ui: _*)
    session.render()

    val calculation = Calculation
      .newCalculation(calc)
      .whenStartingCalculationUpdateUi:
        badge.text = "Calculating..."
        session.render()
      .whenCalculatedUpdateUi: data =>
        badge.text = "Ready"
        renderer(data)
        session.render()
      .build

    calculation
