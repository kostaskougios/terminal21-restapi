package org.terminal21.model

sealed trait ClientToServer

case class SubscribeTo(session: Session) extends ClientToServer
