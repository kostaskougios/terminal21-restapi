package org.terminal21.client

import functions.fibers.{Fiber, FiberExecutor}

abstract class CachedCalculation[OUT](using executor: FiberExecutor) extends Calculation[OUT]:
  def isCached: Boolean
  def invalidateCache(): Unit
  def nonCachedCalculation: OUT
  def reCalculate(): Fiber[OUT] =
    invalidateCache()
    run()
