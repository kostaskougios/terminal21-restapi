package org.terminal21.config

case class Config(host: String, port: Int)

object Config:
  private def host = sys.env.getOrElse("TERMINAL21_HOST", "localhost")
  private def port = sys.env.getOrElse("TERMINAL21_PORT", "8080").toInt

  val Default = Config(host, port)
