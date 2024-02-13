package org.terminal21.ui.std

import org.terminal21.model.{Session, SessionOptions}

/** //> exported
  */
trait SessionsService:
  def createSession(id: String, name: String, sessionOptions: SessionOptions): Session
  def terminateSession(session: Session): Unit

  def setSessionJsonState(session: Session, state: ServerJson): Unit
  def changeSessionJsonState(session: Session, state: ServerJson): Unit
