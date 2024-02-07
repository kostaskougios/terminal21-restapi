package org.terminal21.serverapp

trait ServerSideApp:
  def name: String
  def createSession(serverSideSessions: ServerSideSessions): Unit
