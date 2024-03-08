#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A bouncing ball similar to the C64 basic program. Can we make a bouncing
// ball with the same simplicity as that program?
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.model.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

Sessions
  .withNewSession("bouncing-ball", "C64 bouncing ball")
  .connect: session =>
    given ConnectedSession = session

    // We'll do this with an MVC approach. This is our Model:
    case class Ball(x: Int, y: Int, dx: Int, dy: Int):
      def nextPosition: Ball =
        val newDx = if x < 0 || x > 600 then -dx else dx
        val newDy = if y < 0 || y > 500 then -dy else dy
        Ball(x + newDx, y + newDy, newDx, newDy)

    // In order to update the ball's position, we will be sending approx 60 Ticker events per second to our controller.
    case object Ticker extends ClientEvent

    val initialModel = Ball(50, 50, 8, 8)

    println(
      "Files under ~/.terminal21/web will be served under /web . Please place a ball.png file under ~/.terminal21/web/images on the box where the server runs."
    )

    // This is our controller implementation. It takes the model (ball) and events (in this case just the Ticker which we can otherwise ignore)
    // and results in the next frame's state.
    def components(ball: Ball, events: Events): MV[Ball] =
      val b = ball.nextPosition
      MV(
        b,
        Image(src = "/web/images/ball.png").withStyle("position" -> "fixed", "left" -> (b.x + "px"), "top" -> (b.y + "px"))
      )
    // We'll be sending a Ticker 60 times per second
    fiberExecutor.submit:
      while !session.isClosed do
        session.fireEvent(Ticker)
        Thread.sleep(1000 / 60)

    // We are ready to create a controller instance with our components function.
    Controller(components)
      .render(initialModel) // and render it with our initial model (it will call the components function and render any resulting UI)
      .run() // and run this until the user closes the session
