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

  def withClient[R](id: String, serverPort: Int, executor: FiberExecutor)(f: ClientWsListener[String] => R): R =
    val uri      = URI.create(s"ws://localhost:$serverPort")
    val wsClient = WsClient
      .builder()
      .baseUri(uri)
      .build()
    createClient(id, wsClient, executor)(f)

  def createClient[R](id: String, wsClient: WsClient, executor: FiberExecutor)(f: ClientWsListener[String] => R) =
    Using.resource(ReliableClientWsListener.client(id, wsClient, "/ws-test", executor)): clientWsListener =>
      f(stringClient(clientWsListener))

  def stringClient(clientWsListener: ClientWsListener[BufferData]) =
    clientWsListener.transform(_.map(buf => new String(buf.readBytes(), "UTF-8")), s => BufferData.create(s.getBytes("UTF-8")))

  def runServerClient[R](clientId: String)(test: (ServerWsListener, ClientWsListener[String]) => R): R =
    FiberExecutor.withFiberExecutor: executor =>
      withServer(executor): (server, serverWsListener) =>
        withClient(clientId, server.port, executor)(clientWsListener => test(serverWsListener, clientWsListener))

  test("client sends server a msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      clientWsListener.send("Hello")
      serverWsListener.receivedIterator
        .map: (id, buf) =>
          (id, new String(buf.readBytes()))
        .take(1)
        .toList should be(Seq(("client-1", "Hello")))

  test("multiple clients sends server messages"):
    FiberExecutor.withFiberExecutor: executor =>
      withServer(executor): (server, serverWsListener) =>
        val uri      = URI.create(s"ws://localhost:${server.port}")
        val wsClient = WsClient
          .builder()
          .baseUri(uri)
          .build()
        val fibers   = for i <- 1 to 10 yield executor.submit:
          createClient(s"client-$i", wsClient, executor): client =>
            client.send(s"hello-$i")
            client.receivedIterator.next() should be(s"got hello-$i")

        val serverFiber = executor.submit:
          for (id, msg) <- serverWsListener.receivedIterator do serverWsListener.send(id, BufferData.create(s"got $msg".getBytes))

        // make sure no exceptions
        try for f <- fibers yield f.get()
        finally serverFiber.interrupt()

  test("server sends client a msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      while !serverWsListener.listener.hasClientId("client-1") do Thread.sleep(5)
      serverWsListener.send("client-1", BufferData.create("Hello"))
      clientWsListener.receivedIterator
        .take(1)
        .toList should be(Seq("Hello"))
