package org.terminal21.client

import functions.fibers.FiberExecutor

class Calculation[IN, OUT] private (
    executor: FiberExecutor,
    calc: IN => OUT,
    uiUpdaterWhenResultsNotReady: () => Unit,
    uiUpdaterWhenResultsReady: OUT => Unit,
    notifyWhenCalcReady: Seq[Calculation[OUT, _]]
):
  def apply(in: IN): OUT =
    val f = executor.submit:
      executor.submit:
        uiUpdaterWhenResultsNotReady()

      val out = calc(in)
      executor.submit:
        uiUpdaterWhenResultsReady(out)

      for c <- notifyWhenCalcReady do
        executor.submit:
          try c(out)
          catch case t: Throwable => t.printStackTrace()
      out
    f.get()

object Calculation:
  class CalcBuilder[IN, OUT](
      executor: FiberExecutor,
      calc: IN => OUT,
      uiUpdater: () => Unit = () => (),
      uiReadyUpdater: OUT => Unit = (_: OUT) => (),
      notify: Seq[Calculation[OUT, _]] = Nil
  ):
    def whenStartingCalculationUpdateUi(uiUpdater: => Unit) = new CalcBuilder(executor, calc, () => uiUpdater, uiReadyUpdater, notify)
    def whenCalculatedUpdateUi(uiReadyUpdater: OUT => Unit) = new CalcBuilder(executor, calc, uiUpdater, uiReadyUpdater, notify)
    def notifyAfterCalculated(other: Calculation[OUT, _])   = new CalcBuilder(executor, calc, uiUpdater, uiReadyUpdater, notify :+ other)
    def build: Calculation[IN, OUT]                         = new Calculation[IN, OUT](executor, calc, uiUpdater, uiReadyUpdater, notify)

  def newOutOnlyCalculation[OUT](calc: => OUT)(using executor: FiberExecutor): CalcBuilder[Unit, OUT] =
    new CalcBuilder(executor, _ => calc)

  def newCalculation[IN, OUT](calc: IN => OUT)(using executor: FiberExecutor): CalcBuilder[IN, OUT] =
    new CalcBuilder(executor, calc)
