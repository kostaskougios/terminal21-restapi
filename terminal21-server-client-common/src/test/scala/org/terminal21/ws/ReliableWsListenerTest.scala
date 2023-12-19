package org.terminal21.ws

import functions.fibers.FiberExecutor
import io.helidon.common.buffers.BufferData
import io.helidon.webclient.websocket.WsClient
import io.helidon.webserver.WebServer
import io.helidon.webserver.websocket.WsRouting
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

import java.net.URI
import scala.util.Using

class ReliableWsListenerTest extends AnyFunSuiteLike:
  def withServer[R](executor: FiberExecutor)(f: (WebServer, ServerWsListener) => R): R =
    Using.resource(ReliableServerWsListener.server(executor)): serverWsListener =>
      val wsB    = WsRouting.builder().endpoint("/ws-test", serverWsListener.listener)
      val server = WebServer.builder
        .port(0)
        .addRouting(wsB)
        .build
        .start
      try
        f(server, serverWsListener)
      finally server.stop()

  def withClient[R](id: String, serverPort: Int, executor: FiberExecutor)(f: ClientWsListener => R): R =
    val uri      = URI.create(s"ws://localhost:$serverPort")
    val wsClient = WsClient
      .builder()
      .baseUri(uri)
      .build()
    Using.resource(ReliableClientWsListener.client(id, wsClient, "/ws-test", executor)): clientWsListener =>
      f(clientWsListener)

  def runServerClient[R](clientId: String)(test: (ServerWsListener, ClientWsListener) => R): R =
    FiberExecutor.withFiberExecutor: executor =>
      withServer(executor): (server, serverWsListener) =>
        withClient(clientId, server.port, executor)(clientWsListener => test(serverWsListener, clientWsListener))

  test("client sends server a msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      clientWsListener.send(BufferData.create("Hello"))
      serverWsListener.receivedIterator
        .map: (id, buf) =>
          (id, new String(buf.readBytes()))
        .take(1)
        .toList should be(Seq(("client-1", "Hello")))

  test("server sends client a msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      while !serverWsListener.listener.hasClientId("client-1") do Thread.sleep(5)
      serverWsListener.send("client-1", BufferData.create("Hello"))
      clientWsListener.receivedIterator
        .map: buf =>
          new String(buf.readBytes())
        .take(1)
        .toList should be(Seq("Hello"))
