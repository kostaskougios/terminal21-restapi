package org.terminal21.client

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

    val eventsWsListener = new EventsWsListener(session)
    wsClient.connect("/api/command-ws", eventsWsListener)

    try
      f(ConnectedSession(session, sessionsService))
    finally sessionsService.terminateSession(session)
