package org.terminal21.client.components

import org.terminal21.client.components.UiElement.HasChildren

import java.util.concurrent.atomic.AtomicInteger

object Keys:
  private val id      = new AtomicInteger(0)
  def nextKey: String = "key-" + id.incrementAndGet()
