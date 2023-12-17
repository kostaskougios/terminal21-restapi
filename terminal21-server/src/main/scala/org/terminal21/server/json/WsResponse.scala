package org.terminal21.server.json

import io.circe.Json
import org.terminal21.model.Session

sealed trait WsResponse

case class SessionsWsResponse(sessions: Seq[Session]) extends WsResponse

case class StateWsResponse(session: Session, sessionState: Json) extends WsResponse
