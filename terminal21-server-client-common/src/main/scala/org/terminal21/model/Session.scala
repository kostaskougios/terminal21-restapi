package org.terminal21.model

case class Session(id: String, name: String, secret: String, isOpen: Boolean):
  def hideSecret: Session = copy(secret = "***")
  def close: Session      = copy(isOpen = false)
