package examples

import functions.fibers.FiberExecutor
import io.helidon.webclient.websocket.WsClient
import org.terminal21.client.ws.TwoWayWsListener
import org.terminal21.config.Config

import java.net.URI
import scala.util.Using

@main def tryWebSocket(): Unit =
  FiberExecutor.withFiberExecutor: executor =>
    val config    = Config.Default
    val uri       = URI.create(s"ws://${config.host}:${config.port}")
    println(uri)
    val webClient = WsClient
      .builder()
      .baseUri(uri)
      .build()

    Using.resource(new TwoWayWsListener(executor)): twoWayListener =>
      webClient.connect("/api/client-ws", twoWayListener)

      val sendAndReceive = twoWayListener.senderAndReceiver
      println("Sending")
      sendAndReceive.send("dude")
      println("Receiving")
      println(sendAndReceive.receive)

      twoWayListener.close()
      Thread.sleep(1000)
      println("terminating")
