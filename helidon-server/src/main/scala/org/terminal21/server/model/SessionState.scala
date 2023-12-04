package org.terminal21.server.model

import org.terminal21.server.json.WsResponse
import org.terminal21.ui.std.model.Session

case class SessionState(session: Session, responses: Seq[WsResponse] = Nil):
  def addResponse(wsResponse: WsResponse): SessionState = copy(responses = responses :+ wsResponse)

  def waitChange(): Unit    = synchronized(wait())
  def notifyChanged(): Unit = synchronized(notifyAll())
