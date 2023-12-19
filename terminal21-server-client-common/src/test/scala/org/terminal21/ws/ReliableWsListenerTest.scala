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
  case class ServerValue(id: String, value: String)
  def withServer[R](executor: FiberExecutor)(f: (WebServer, ServerWsListener[ServerValue]) => R): R =
    Using.resource(ReliableServerWsListener.server(executor)): serverWsListener =>
      val wsB            = WsRouting.builder().endpoint("/ws-test", serverWsListener.listener)
      val server         = WebServer.builder
        .port(0)
        .addRouting(wsB)
        .build
        .start
      val stringListener =
        serverWsListener.transform(
          _.map((id, value) => ServerValue(id, new String(value.readBytes(), "UTF-8"))),
          sv => (sv.id, BufferData.create(sv.value.getBytes))
        )
      try
        f(server, stringListener)
      finally server.stop()

  def withClient[R](id: String, serverPort: Int, executor: FiberExecutor)(f: ClientWsListener[String] => R): R =
    val wsClient = newWsClient(serverPort)
    createClient(id, wsClient, executor)(f)

  def newWsClient(serverPort: Int) =
    val uri = URI.create(s"ws://localhost:$serverPort")
    WsClient
      .builder()
      .baseUri(uri)
      .build()

  def createClient[R](id: String, wsClient: WsClient, executor: FiberExecutor)(f: ClientWsListener[String] => R) =
    Using.resource(ReliableClientWsListener.client(id, wsClient, "/ws-test", executor)): clientWsListener =>
      f(stringClient(clientWsListener))

  def stringClient(clientWsListener: ClientWsListener[BufferData]) =
    clientWsListener.transform(_.map(buf => new String(buf.readBytes(), "UTF-8")), s => BufferData.create(s.getBytes("UTF-8")))

  def runServerClient[R](clientId: String)(test: (ServerWsListener[ServerValue], ClientWsListener[String]) => R): R =
    FiberExecutor.withFiberExecutor: executor =>
      withServer(executor): (server, serverWsListener) =>
        withClient(clientId, server.port, executor)(clientWsListener => test(serverWsListener, clientWsListener))

  test("client sends server a msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      clientWsListener.send("Hello")
      serverWsListener.receivedIterator
        .take(1)
        .toList should be(Seq(ServerValue("client-1", "Hello")))

  test("multiple clients sends server messages"):
    FiberExecutor.withFiberExecutor: executor =>
      withServer(executor): (server, serverWsListener) =>
        val wsClient = newWsClient(server.port)
        val fibers   = for i <- 1 to 40 yield executor.submit:
          createClient(s"client-$i", wsClient, executor): client =>
            for j <- 1 to 100 do
              client.send(s"hello-$i-$j")
              client.receivedIterator.next() should be(s"got hello-$i-$j")

        val serverFiber = executor.submit:
          for sv <- serverWsListener.receivedIterator do serverWsListener.send(ServerValue(sv.id, s"got ${sv.value}"))

        // make sure no exceptions
        try for f <- fibers yield f.get()
        finally serverFiber.interrupt()

  test("server sends client a msg"):
    runServerClient("client-1"): (serverWsListener, clientWsListener) =>
      while !serverWsListener.listener.hasClientId("client-1") do Thread.sleep(5)
      serverWsListener.send(ServerValue("client-1", "Hello"))
      clientWsListener.receivedIterator
        .take(1)
        .toList should be(Seq("Hello"))
