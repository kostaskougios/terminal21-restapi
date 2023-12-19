package org.terminal21.ws

import io.helidon.common.buffers.BufferData

trait Transformer[A, B]:
  def transform(a: A): B
  def reverse(b: B): A

trait BufferDataTransformer[B] extends Transformer[BufferData, B]
