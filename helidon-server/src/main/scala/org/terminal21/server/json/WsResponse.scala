package org.terminal21.server.json

import io.circe.*
import io.circe.generic.auto.*

sealed trait WsResponse

case class Initialized(msg: String) extends WsResponse

object WsResponse:
  val encoder = Encoder[WsResponse]
