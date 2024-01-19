package org.terminal21.server.json

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*

case class WsRequest(operation: String, body: Option[Body])

sealed trait Body

case class SessionFullRefresh(sessionId: String) extends Body

sealed trait UiEvent extends Body:
  def sessionId: String

case class OnClick(sessionId: String, key: String)                 extends UiEvent
case class OnChange(sessionId: String, key: String, value: String) extends UiEvent

case class CloseSession(id: String)  extends Body
case class RemoveSession(id: String) extends Body

object WsRequest:
  val decoder = decode[WsRequest]
