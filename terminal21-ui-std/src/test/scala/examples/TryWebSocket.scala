package examples

import io.helidon.webclient.websocket.WsClient
import io.helidon.websocket.{WsListener, WsSession}
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

  webClient.connect("/api/client-ws", MyListener)
  Thread.sleep(2000)
  println("terminating")

object MyListener extends WsListener:
  override def onOpen(session: WsSession) =
    println(s"onOpen: $session")
    session.send("Hello!", false)
    session.send("World!", true)
