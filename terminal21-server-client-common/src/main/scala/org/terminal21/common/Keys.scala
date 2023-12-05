package org.terminal21.common

import java.util.UUID

object Keys:
  def randomKey: String = UUID.randomUUID().toString
