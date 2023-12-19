package org.terminal21.ws

import io.helidon.common.buffers.BufferData

trait Transformer[A, B]:
  def map(a: A): B

trait BufferDataTransformer[A] extends Transformer[BufferData, A]
