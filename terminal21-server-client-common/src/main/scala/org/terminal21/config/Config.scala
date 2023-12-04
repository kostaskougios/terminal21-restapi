package org.terminal21.config

case class Config(host: String, port: Int)

object Config:
  val Default = Config("localhost", 8080)
