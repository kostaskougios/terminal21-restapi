#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.SessionOptions

Sessions
  .withNewSession("on-click-example", "On Click Handler")
  .andOptions(SessionOptions.LeaveOpenWhenTerminated) // leave the app UI visible after we terminate so that we can see the final "Button clicked" message
  .connect: session =>
    given ConnectedSession = session

    val msg = Paragraph(text = "Waiting for user to click the button")
    val button = Button(text = "Please click me")
    Seq(msg, button).render()

    case class Model(isButtonClicked: Boolean)

    session.eventIterator // get an iterator for all events
      .map(e => Model(e.isTarget(button))) // convert it to our model
      .dropWhile(!_.isButtonClicked) // ignore all events until the user clicks our button
      .nextOption() match
      case None        => // the user closed the app
      case Some(model) => msg.withText(s"Button clicked. Model = $model").renderChanges() // the user for sure clicked the button
