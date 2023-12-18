package org.terminal21.ws

import functions.fibers.FiberExecutor
import io.helidon.common.buffers.BufferData
import io.helidon.webclient.websocket.WsClient
import io.helidon.webserver.WebServer
import io.helidon.webserver.websocket.WsRouting
import org.scalatest.funsuite.AnyFunSuiteLike

import java.net.URI
import scala.util.Using

class ReliableWsListenerTest extends AnyFunSuiteLike:
  def withServer[R](executor: FiberExecutor, onReceive: (String, BufferData) => Unit)(f: (WebServer, ServerWsListener) => R): R =
    Using.resource(ReliableServerWsListener.server(executor)(onReceive)): serverWsListener =>
      val wsB    = WsRouting.builder().endpoint("/ws-test", serverWsListener.listener)
      val server = WebServer.builder
        .port(0)
        .addRouting(wsB)
        .build
        .start
      try
        f(server, serverWsListener)
      finally server.stop()

  def withClient[R](id: String, serverPort: Int, executor: FiberExecutor, onReceive: BufferData => Unit)(f: ClientWsListener => R): R =
    val uri      = URI.create(s"ws://localhost:$serverPort")
    val wsClient = WsClient
      .builder()
      .baseUri(uri)
      .build()
    Using.resource(ReliableClientWsListener.client(id, wsClient, "/ws-test", executor)(onReceive)): clientWsListener =>
      f(clientWsListener)

  def runServerClient[R](clientId: String)(test: (ServerWsListener, ClientWsListener) => R): R =
    FiberExecutor.withFiberExecutor: executor =>
      withServer(executor, (id, data) => println(s"Server received from $id data [${new String(data.readBytes())}]")): (server, serverWsListener) =>
        withClient(clientId, server.port, executor, data => println(s"client received $data"))(clientWsListener => test(serverWsListener, clientWsListener))

  test("client-server msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      clientWsListener.sender(BufferData.create("Hello"))
      Thread.sleep(2000)
