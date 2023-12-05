package org.terminal21.server.model

import org.terminal21.server.json.WsResponse
import org.terminal21.ui.std.model.Session

case class SessionState(
    session: Session,
    responses: Seq[WsResponse] = Nil
):
  def addResponse(wsResponse: WsResponse): SessionState =
    val newResponses = responses.map: r =>
      if r.key == wsResponse.key then wsResponse else r

    val hasKey = newResponses.exists(_.key == wsResponse.key)
    val res    = if hasKey then newResponses else newResponses :+ wsResponse
    copy(responses = res)
