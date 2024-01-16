package org.terminal21.server.json

import org.terminal21.model.Session
import org.terminal21.ui.std.ServerJson

sealed trait WsResponse

case class SessionsWsResponse(sessions: Seq[Session]) extends WsResponse

case class StateWsResponse(session: Session, sessionState: ServerJson) extends WsResponse
