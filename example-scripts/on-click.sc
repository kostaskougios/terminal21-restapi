#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*

Sessions
  .withNewSession("on-click-example", "On Click Handler")
  .connect: session =>
    given ConnectedSession = session

    @volatile var exit = false
    val msg = Paragraph(text = "Waiting for user to click the button")
    val button = Button(text = "Please click me").onClick: () =>
      msg.withText("Button clicked.").renderChanges()
      exit = true

    Seq(msg, button).render()

    session.waitTillUserClosesSessionOr(exit)
