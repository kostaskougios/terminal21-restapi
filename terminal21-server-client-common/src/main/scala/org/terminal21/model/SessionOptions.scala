package org.terminal21.model

case class SessionOptions(closeTabWhenTerminated: Boolean = false, alwaysOpen: Boolean = false)

object SessionOptions:
  val Defaults = SessionOptions()
