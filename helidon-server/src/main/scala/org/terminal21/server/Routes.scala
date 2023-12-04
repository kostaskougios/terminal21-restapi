package org.terminal21.server

import io.helidon.webserver.http.HttpRouting
import org.terminal21.server.Dependencies
import org.terminal21.ui.std.SessionsServiceReceiverFactory

object Routes:
  def register(dependencies: Dependencies, rb: HttpRouting.Builder): Unit =
    import dependencies.*
    val sessionRoutes = SessionsServiceReceiverFactory.newJsonSessionsServiceHelidonRoutes(sessionsService)
    sessionRoutes.routes(rb)
