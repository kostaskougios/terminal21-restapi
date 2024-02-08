package org.terminal21.client

import functions.fibers.FiberExecutor
import functions.helidon.transport.HelidonTransport
import io.helidon.webclient.api.WebClient
import io.helidon.webclient.websocket.WsClient
import org.terminal21.client.components.{ComponentLib, StdElementEncoding, UiElementEncoding}
import org.terminal21.config.Config
import org.terminal21.ui.std.SessionsServiceCallerFactory

import java.util.concurrent.atomic.AtomicBoolean
import scala.util.Using.Releasable

object Sessions:
  case class SessionBuilder(id: String, name: String, componentLibs: Seq[ComponentLib] = Seq(StdElementEncoding)):
    def andLibraries(libraries: ComponentLib*): SessionBuilder = copy(componentLibs = componentLibs ++ libraries)

    def connect[R](f: ConnectedSession => R): R =
      val config          = Config.Default
      val serverUrl       = s"http://${config.host}:${config.port}"
      val client          = WebClient.builder
        .baseUri(serverUrl)
        .build
      val transport       = new HelidonTransport(client)
      val sessionsService = SessionsServiceCallerFactory.newHelidonJsonSessionsService(transport)
      val session         = sessionsService.createSession(id, name)
      val wsClient        = WsClient.builder
        .baseUri(s"ws://${config.host}:${config.port}")
        .build

      val isStopped = new AtomicBoolean(false)

      def terminate(): Unit =
        isStopped.set(true)

      val encoding         = new UiElementEncoding(Seq(StdElementEncoding) ++ componentLibs)
      val connectedSession = ConnectedSession(session, encoding, serverUrl, sessionsService, terminate)
      FiberExecutor.withFiberExecutor: executor =>
        val listener = new ClientEventsWsListener(wsClient, connectedSession, executor)
        listener.start()

        try f(connectedSession)
        finally
          if !isStopped.get() && !connectedSession.isLeaveSessionOpen then sessionsService.terminateSession(session)
          listener.close()

  def withNewSession(id: String, name: String): SessionBuilder = SessionBuilder(id, name)
