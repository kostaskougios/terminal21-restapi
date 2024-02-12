package org.terminal21.client.components

import java.util.UUID

object TransientRequest:
  def newRequestId(): String = UUID.randomUUID().toString
