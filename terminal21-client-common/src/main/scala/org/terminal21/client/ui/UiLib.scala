package org.terminal21.client.ui

import io.circe.Json

trait UiLib:
  def toJson(e: UiElement): Option[Json]
