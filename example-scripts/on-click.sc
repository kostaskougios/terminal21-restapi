#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.SessionOptions

case class ClickForm(clicked: Boolean)

Sessions
  .withNewSession("on-click-example", "On Click Handler")
  .connect: session =>
    given ConnectedSession = session
    given Model[ClickForm] = Model(ClickForm(false))

    val msg = Paragraph(text = "Waiting for user to click the button")
    val button = Button(text = "Please click me").onClick: event =>
      import event.*
      handled.withModel(model.copy(clicked = true)).withRenderChanges(msg.withText("Button clicked")).terminate

    Controller(
      Seq(msg, button)
    ).render().handledEventsIterator.lastOption.map(_.model) match
      case None        => // the user closed the app
      case Some(model) => println(s"model = $model")

    session.leaveSessionOpenAfterExiting() // leave the session open after exiting so that the user can examine the UI
