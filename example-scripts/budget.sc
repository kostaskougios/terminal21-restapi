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

case class BudgetForm(startDate: LocalDate = LocalDate.of(2024, 4, 1), initialBudget: Int = 1000, percentIncreasePerYear: Float = 4f / 100f)

class BudgetPage(initialForm: BudgetForm)(using ConnectedSession):
  def run: Option[BudgetForm] =
    controller.render(initialForm).run()

  def components(form: BudgetForm, events: Events): MV[BudgetForm] =

    case class Row(date: LocalDate, budget: Float, total: Float)
    val rows = (1 to 30 * 12).foldLeft(Seq.empty[Row]): (rows, month) =>
      val date = form.startDate.plusMonths(month)
      val budget = form.initialBudget + form.initialBudget * ((month / 12) * form.percentIncreasePerYear)
      val total = rows.map(_.budget).sum + budget
      rows :+ Row(date, budget, total)
    val table = QuickTable().withHeaders("Date", "Budget", "Total").withRows(rows.map(r => Seq(r.date, r.budget, r.total)))

    MV(form, table)

  def controller: Controller[BudgetForm] = Controller(components)
