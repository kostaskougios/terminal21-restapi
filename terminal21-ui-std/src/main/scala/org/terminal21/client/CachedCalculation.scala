package org.terminal21.client

import functions.fibers.FiberExecutor

abstract class CachedCalculation[IN, OUT](
    notifyWhenCalcReady: Seq[Calculation[OUT, _]]
)(using executor: FiberExecutor)
    extends Calculation[IN, OUT](notifyWhenCalcReady):
  def isCached: Boolean
  def invalidateCache(): Unit
