package org.terminal21.serverapp

import org.terminal21.server.Dependencies

trait ServerSideApp:
  def name: String
  def description: String
  def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit
