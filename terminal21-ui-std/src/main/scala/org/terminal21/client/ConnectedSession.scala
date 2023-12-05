package org.terminal21.client

import functions.helidon.transport.HelidonTransport
import org.terminal21.ui.std.SessionsService
import org.terminal21.ui.std.model.Session

case class ConnectedSession(session: Session, transport: HelidonTransport, sessionsService: SessionsService):
  def use[T](using factory: ConnectedSession => T): T = factory(this)
