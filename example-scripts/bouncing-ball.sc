#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A bouncing ball similar to the C64 basic program. Can we make a bouncing
// ball with the same simplicity as that program?
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import scala.annotation.tailrec

Sessions.withNewSession("bouncing-ball", "C64 bouncing ball"): session =>
  given ConnectedSession = session

  // Files under ~/.terminal21/web will be served under /web . Please place a ball.png file under ~/.terminal21/web/images on the box where the server runs.
  val ball = Image(src = "/web/images/ball.png")
  ball.render()

  @tailrec def animateBall(x: Int, y: Int, dx: Int, dy: Int): Unit =
    ball.withStyle("position" -> "fixed", "left" -> (x + "px"), "top" -> (y + "px")).renderChanges()
    Thread.sleep(1000 / 120)
    val newDx = if x < 0 || x > 600 then -dx else dx
    val newDy = if y < 0 || y > 500 then -dy else dy
    animateBall(x + newDx, y + newDy, newDx, newDy)

  animateBall(50, 50, 8, 8)
