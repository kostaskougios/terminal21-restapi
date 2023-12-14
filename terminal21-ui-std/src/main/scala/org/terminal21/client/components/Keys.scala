package org.terminal21.client.components

import java.util.concurrent.atomic.AtomicInteger

object Keys:
  private val keyId   = new AtomicInteger(0)
  def nextKey: String = s"key${keyId.incrementAndGet()}"
