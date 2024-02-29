#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A bouncing ball similar to the C64 basic program. Can we make a bouncing
// ball with the same simplicity as that program?
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.{*, given}
import org.terminal21.client.components.*
import org.terminal21.model.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

Sessions
  .withNewSession("bouncing-ball", "C64 bouncing ball")
  .connect: session =>
    given ConnectedSession = session

    case class Ball(x: Int, y: Int, dx: Int, dy: Int):
      def nextPosition: Ball =
        val newDx = if x < 0 || x > 600 then -dx else dx
        val newDy = if y < 0 || y > 500 then -dy else dy
        Ball(x + newDx, y + newDy, newDx, newDy)
    case object Ticker extends ClientEvent

    given Model[Ball] = Model(Ball(50, 50, 8, 8))

    println(
      "Files under ~/.terminal21/web will be served under /web . Please place a ball.png file under ~/.terminal21/web/images on the box where the server runs."
    )
    val ball = Image(src = "/web/images/ball.png").onModelChange: (b, m) =>
      b.withStyle("position" -> "fixed", "left" -> (m.x + "px"), "top" -> (m.y + "px"))

    fiberExecutor.submit:
      while !session.isClosed do
        session.fireEvent(Ticker)
        Thread.sleep(1000 / 60)

    Controller(Seq(ball))
      .onEvent:
        case ControllerClientEvent(handled, Ticker) =>
          handled.withModel(handled.model.nextPosition)
      .render()
      .handledEventsIterator
      .lastOption
