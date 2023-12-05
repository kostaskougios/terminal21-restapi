package org.terminal21.server.json

import io.circe.*
import io.circe.generic.auto.*
import org.terminal21.ui.std.json.StdElement

sealed trait WsResponse:
  def key: String

// std react/html components
case class Std(element: StdElement) extends WsResponse:
  override def key: String = element.key
