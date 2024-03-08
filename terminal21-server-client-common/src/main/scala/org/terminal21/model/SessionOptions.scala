package org.terminal21.model

case class SessionOptions(closeTabWhenTerminated: Boolean = true, alwaysOpen: Boolean = false)

object SessionOptions:
  val Defaults                = SessionOptions()
  val LeaveOpenWhenTerminated = SessionOptions(closeTabWhenTerminated = false)
