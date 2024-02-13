package org.terminal21.model

object CommonModelBuilders:
  def session(
      id: String = "session-id",
      name: String = "session-name",
      secret: String = "session-secret",
      isOpen: Boolean = true,
      sessionOptions: SessionOptions = SessionOptions.Defaults
  ) =
    Session(id, name, secret, isOpen, sessionOptions)
