package org.terminal21.client

import functions.fibers.FiberExecutor

import java.util.concurrent.CountDownLatch

abstract class Calculation[IN, OUT](
    notifyWhenCalcReady: Seq[Calculation[OUT, _]]
)(using executor: FiberExecutor):
  protected def calculation(in: IN): OUT
  protected def whenResultsNotReady(): Unit =
    for c <- notifyWhenCalcReady do c.whenResultsNotReady()

  protected def whenResultsReady(results: OUT): Unit

  def run(in: IN): OUT =
    val refreshInOrder = new CountDownLatch(1)
    val f              = executor.submit:
      executor.submit:
        try whenResultsNotReady()
        finally refreshInOrder.countDown()

      val out = calculation(in)
      refreshInOrder.await()
      postRun(out)
      out
    f.get()

  protected def postRun(out: OUT): Unit =
    executor.submit:
      whenResultsReady(out)

    for c <- notifyWhenCalcReady do
      executor.submit:
        try c.run(out)
        catch case t: Throwable => t.printStackTrace()

object Calculation:
  class Builder[IN, OUT](
      calc: IN => OUT,
      uiNotReadyUpdater: () => Unit = () => (),
      uiReadyUpdater: OUT => Unit = (_: OUT) => (),
      notify: Seq[Calculation[OUT, _]] = Nil
  )(using executor: FiberExecutor):
    def whenResultsNotReady(uiUpdater: => Unit)           = new Builder(calc, () => uiUpdater, uiReadyUpdater, notify)
    def whenResultsReady(uiReadyUpdater: OUT => Unit)     = new Builder(calc, uiNotReadyUpdater, uiReadyUpdater, notify)
    def notifyAfterCalculated(other: Calculation[OUT, _]) = new Builder(calc, uiNotReadyUpdater, uiReadyUpdater, notify :+ other)
    def build: Calculation[IN, OUT]                       =
      new Calculation[IN, OUT](notify):
        override protected def calculation(in: IN): OUT             = calc(in)
        override protected def whenResultsNotReady(): Unit          = uiNotReadyUpdater()
        override protected def whenResultsReady(results: OUT): Unit = uiReadyUpdater(results)

  def newCalculationNoIn[OUT](calc: => OUT)(using executor: FiberExecutor): Builder[Unit, OUT] =
    new Builder(_ => calc)

  def newCalculation[IN, OUT](calc: IN => OUT)(using executor: FiberExecutor): Builder[IN, OUT] =
    new Builder(calc)
