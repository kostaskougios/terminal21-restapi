#!/usr/bin/env -S scala-cli project.scala
// ------------------------------------------------------------------------------
// Hello world with terminal21.
// Run with ./hello-world.sc
// ------------------------------------------------------------------------------

import org.terminal21.client.*
import org.terminal21.client.components.*
// std components like Paragraph, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.std.*

Sessions
  .withNewSession("hello-world", "Hello World Example")
  .connect: session =>
    given ConnectedSession = session

    Controller.noModel(Paragraph(text = "Hello World!")).render()
    // since this is a read-only UI, we can exit the app but leave the session open on the UI for the user to examine the data.
    session.leaveSessionOpenAfterExiting()
