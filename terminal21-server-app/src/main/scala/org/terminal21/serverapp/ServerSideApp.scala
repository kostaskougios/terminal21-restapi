package org.terminal21.serverapp

trait ServerSideApp:
  def createSession(serverSideSessions: ServerSideSessions): Unit
