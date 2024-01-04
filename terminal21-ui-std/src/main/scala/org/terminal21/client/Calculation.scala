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

  def notifyCalc(calcToNotify: Calculation[OUT, _]): Calculation[IN, OUT] =
    new Calculation(executor, calc, uiUpdaterWhenResultsNotReady, uiUpdaterWhenResultsReady, notifyWhenCalcReady :+ calcToNotify)

object Calculation:
  def apply[IN, OUT](
      calc: IN => OUT,
      uiUpdaterWhenResultsNotReady: () => Unit,
      uiUpdaterWhenResultsReady: OUT => Unit,
      notify: Seq[Calculation[OUT, _]] = Nil
  )(using
      executor: FiberExecutor
  ): Calculation[IN, OUT] =
    new Calculation(executor, calc, uiUpdaterWhenResultsNotReady, uiUpdaterWhenResultsReady, notify)
