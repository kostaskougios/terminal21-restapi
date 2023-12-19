package org.terminal21.client

import functions.fibers.FiberExecutor
import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient
import io.helidon.webclient.websocket.WsClient
import org.terminal21.config.Config
import org.terminal21.ui.std.SessionsServiceCallerFactory

object Sessions:
  def withNewSession[R](id: String, name: String)(f: ConnectedSession => R): R =
    val config          = Config.Default
    val client          = WebClient.builder
      .baseUri(s"http://${config.host}:${config.port}")
      .build
    val transport       = new HelidonTransport(client)
    val sessionsService = SessionsServiceCallerFactory.newHelidonJsonSessionsService(transport)
    val session         = sessionsService.createSession(id, name)
    val wsClient        = WsClient.builder
      .baseUri(s"ws://${config.host}:${config.port}")
      .build

    val connectedSession = ConnectedSession(session, sessionsService)
    FiberExecutor.withFiberExecutor: executor =>
      val listener = new ClientEventsWsListener(wsClient, connectedSession, executor)
      listener.start()

      try {
        f(connectedSession)
      } finally
        sessionsService.terminateSession(session)
        listener.close()
