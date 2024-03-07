#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// Universe creation progress bar demo
// Run with ./progress.sc
// ------------------------------------------------------------------------------

import org.terminal21.client.{*, given}
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.{ClientEvent, SessionOptions}

Sessions
  .withNewSession("universe-generation", "Universe Generation Progress")
  .connect: session =>
    given ConnectedSession = session

    def components(model: Int, events: Events): MV[Int] =
      val status =
        if model < 10 then "Generating universe ..."
        else if model < 30 then "Creating atoms"
        else if model < 50 then "Big bang!"
        else if model < 80 then "Inflating"
        else "Life evolution"

      val msg = Paragraph(text = status)
      val progress = Progress(value = model)

      MV(
        model + 1,
        Seq(msg, progress)
      )

    // send a ticker to update the progress bar
    object Ticker extends ClientEvent
    fiberExecutor.submit:
      for _ <- 1 to 100 do
        Thread.sleep(200)
        session.fireEvent(Ticker)

    Controller(components)
      .render(1)
      .iterator
      .takeWhile(_.model < 100) // terminate when model == 100
      .foreach(_ => ()) // and run it
    // clear UI
    session.render(Seq(Paragraph(text = "Universe ready!")))
    session.leaveSessionOpenAfterExiting()
