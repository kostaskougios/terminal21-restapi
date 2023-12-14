package org.terminal21.model

object CommonModelBuilders:
  def session(id: String = "session-id", name: String = "session-name", secret: String = "session-secret") =
    Session(id, name, secret)
