package org.terminal21.ui.std

import org.terminal21.model.Session

/** //> exported
  */
trait SessionsService:
  def createSession(id: String, name: String): Session
  def terminateSession(session: Session): Unit
