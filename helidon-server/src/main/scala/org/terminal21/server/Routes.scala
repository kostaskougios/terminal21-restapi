package org.terminal21.server

import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.staticcontent.StaticContentService
import org.terminal21.ui.std.SessionsServiceReceiverFactory

import java.nio.file.Path

object Routes:
  def register(dependencies: Dependencies, rb: HttpRouting.Builder): Unit =
    import dependencies.*
    val sessionRoutes = SessionsServiceReceiverFactory.newJsonSessionsServiceHelidonRoutes(sessionsService)
    sessionRoutes.routes(rb)

  def static(rb: HttpRouting.Builder): Unit =
    val staticContent = StaticContentService
      .builder(Path.of("../../terminal21-ui/build"))
      .welcomeFileName("index.html")
      .build
    rb.register("/ui", staticContent)
