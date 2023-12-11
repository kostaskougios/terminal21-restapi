package org.terminal21.model

case class Session(id: String, name: String, secret: String):
  def hideSecret: Session = copy(secret = "***")
