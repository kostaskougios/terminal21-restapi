package org.terminal21.client.components

import functions.fibers.{Fiber, FiberExecutor}

import java.util.concurrent.CountDownLatch

trait Calculation[OUT](using executor: FiberExecutor):
  protected def calculation(): OUT
  protected def whenResultsNotReady(): Unit          = ()
  protected def whenResultsReady(results: OUT): Unit = ()

  def reCalculate(): Fiber[OUT] = run()

  def onError(t: Throwable): Unit =
    t.printStackTrace()

  def run(): Fiber[OUT] =
    val refreshInOrder = new CountDownLatch(1)
    executor.submit:
      try
        executor.submit:
          try whenResultsNotReady()
          finally refreshInOrder.countDown()

        val out = calculation()
        refreshInOrder.await()
        executor.submit:
          whenResultsReady(out)
        out
      catch
        case t: Throwable =>
          onError(t)
          throw t
