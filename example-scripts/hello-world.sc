#!/usr/bin/env -S scala-cli project.scala
// ------------------------------------------------------------------------------
// Hello world with terminal21.
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.*

Sessions.withNewSession("hello-world", "Hello World Example"): session =>
  given ConnectedSession = session

  Seq(
    Paragraph(text = "Hello World!")
  ).render()
  session.waitTillUserClosesSession()
