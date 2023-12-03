package org.terminal21.server.json

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*

case class WsRequest(operation: String, body: Option[Body])

sealed trait Body

case class NoBody() extends Body

object WsRequest:
  val decoder = decode[WsRequest]
