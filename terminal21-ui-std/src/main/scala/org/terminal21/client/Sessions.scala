package org.terminal21.client

import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient
import org.terminal21.config.Config
import org.terminal21.ui.std.SessionsServiceCallerFactory

object Sessions:
  def withNewSession[R](id: String, name: String)(f: ConnectedSession => R): R =
    val config          = Config.Default
    val client          = WebClient
      .builder()
      .baseUri(s"http://${config.host}:${config.port}")
      .build()
    val transport       = new HelidonTransport(client)
    val sessionsService = SessionsServiceCallerFactory.newHelidonJsonSessionsService(transport)
    val session         = sessionsService.createSession(id, name)
    try
      f(ConnectedSession(session, transport))
    finally sessionsService.terminateSession(session)
