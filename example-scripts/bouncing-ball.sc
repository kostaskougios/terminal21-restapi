#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A csv file viewer
// ------------------------------------------------------------------------------

//> using dep io.github.kostaskougios::terminal21-ui-std:0.1
//> using dep commons-io:commons-io:2.15.1

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

  val ball = Image(src = "/web/images/ball.png")
  Seq(
    Paragraph(text =
      "There was a very simple C64 basic program that was bouncing a ball into the screen. This reimplements it in scala and terminal21. Unfortunately the imports and deps make it a bit more complicated but it is not bad at all. Just make sure you have your favorite ball.png under ~/.terminal21-ui/web/images/ball.png"
    ),
        Paragraph(text =
      "The original C64 basic program: https://www.commodore.ca/manuals/c64_users_guide/c64-users_guide-04-advanced_basic.pdf"
    ),
    ball
  ).render()

  @tailrec def animateBall(x: Int, y: Int, dx: Int, dy: Int): Unit =
    ball.style = Map("position" -> "fixed", "left" -> (x + "px"), "top" -> (y + "px"))
    session.render()
    Thread.sleep(1000 / 60)
    val newDx = if x < 0 || x > 600 then -dx else dx
    val newDy = if y < 0 || y > 500 then -dy else dy
    animateBall(x + newDx, y + newDy, newDx, newDy)

  animateBall(50, 50, 8, 8)
