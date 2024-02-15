#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.SessionOptions

Sessions
  .withNewSession("universe-generation", "Universe Generation Progress")
  .andOptions(SessionOptions.LeaveOpenWhenTerminated) /* leave the session tab open after terminating */
  .connect: session =>
    given ConnectedSession = session

    val msg = Paragraph(text = "Generating universe ...")
    val progress = Progress(value = 1)

    Seq(msg, progress).render()

    for i <- 1 to 100 do
      val p = progress.withValue(i)
      val m =
        if i < 10 then msg
        else if i < 30 then msg.withText("Creating atoms")
        else if i < 50 then msg.withText("Big bang!")
        else if i < 80 then msg.withText("Inflating")
        else msg.withText("Life evolution")

      Seq(p, m).renderChanges()
      Thread.sleep(100)

    // clear UI
    session.clear()
    Paragraph(text = "Universe ready!").render()
