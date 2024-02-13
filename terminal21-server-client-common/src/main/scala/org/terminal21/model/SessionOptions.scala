package org.terminal21.model

case class SessionOptions(deleteWhenTerminated: Boolean = false)

object SessionOptions:
  val Defaults = SessionOptions()
