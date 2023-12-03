package org.terminal21.server.json

import io.circe.*
import io.circe.generic.auto.*

sealed trait WsResponse

// std react/html components
case class Std(elements: Seq[StdElement]) extends WsResponse

object WsResponse:
  val encoder = Encoder[WsResponse]
