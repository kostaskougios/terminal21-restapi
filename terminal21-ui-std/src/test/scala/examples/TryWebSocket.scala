package examples

import io.helidon.webclient.websocket.WsClient
import org.terminal21.client.ws.TwoWayWsListener
import org.terminal21.config.Config

import java.net.URI

@main def tryWebSocket(): Unit =
  val config    = Config.Default
  val uri       = URI.create(s"ws://${config.host}:${config.port}")
  println(uri)
  val webClient = WsClient
    .builder()
    .baseUri(uri)
    .build()

  val twoWayListener = new TwoWayWsListener[String, String](identity, identity)
  webClient.connect("/api/client-ws", twoWayListener)

  val sendAndReceive = twoWayListener.senderAndReceiver
  println("Sending")
  sendAndReceive.send("dude")
  println("Receiving")
  println(sendAndReceive.receive)
  Thread.sleep(1000)
  println("terminating")
