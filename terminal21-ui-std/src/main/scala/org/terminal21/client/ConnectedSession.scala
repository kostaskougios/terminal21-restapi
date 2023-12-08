package org.terminal21.client

import functions.helidon.transport.HelidonTransport
import org.terminal21.model.Session

class ConnectedSession(val session: Session, val transport: HelidonTransport):
  def use[T](using factory: ConnectedSession => T): T = factory(this)
