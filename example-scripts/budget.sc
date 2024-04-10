#!/usr/bin/env -S scala-cli

//> using file project.scala
import org.terminal21.client.components.chakra.QuickTable
import java.time.LocalDate

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*

Sessions
  .withNewSession("budget", "Personal Budget Calculator")
  .connect: session =>
    given ConnectedSession = session

    println(new BudgetPage(BudgetForm()).run)

case class BudgetForm(
    startDate: LocalDate = LocalDate.of(2024, 4, 1),
    initialBudget: Int = 1000,
    percentIncreasePerYear: Float = 4f / 100f,
    available: Float = 100000
)

class BudgetPage(initialForm: BudgetForm)(using ConnectedSession):
  def run: Option[BudgetForm] =
    controller.render(initialForm).run()

  def components(form: BudgetForm, events: Events): MV[BudgetForm] =

    case class Row(date: LocalDate, budget: Float, total: Float, available: Float)
    val rows = (1 to 30 * 12)
      .foldLeft((Seq.empty[Row], form.initialBudget)):
        case ((rows, lastBudget), month) =>
          val date = form.startDate.plusMonths(month)
          val budget = if month % 12 == 0 then (lastBudget + lastBudget * form.percentIncreasePerYear).toInt else lastBudget
          val total = rows.map(_.budget).sum + budget
          (
            rows :+ Row(date, budget, total, Math.max(0, form.available - total)),
            budget
          )
      ._1
    val table = QuickTable().withHeaders("Date", "Budget", "Total", "Available").withRows(rows.map(r => Seq(r.date, r.budget, r.total, r.available)))

    MV(form, table)

  def controller: Controller[BudgetForm] = Controller(components)
