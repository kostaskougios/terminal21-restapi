package tests

import org.terminal21.client.*
import org.terminal21.client.components.*

@main def stdComponents(): Unit =
  Sessions.withNewSession("std-components", "Std Components"): session =>
    given ConnectedSession = session
    Seq(
      Header1(text = "Welcome to the std components demo/test"),
      Paragraph(text = "Hello World!").withChildren(
        NewLine(),
        Span(text = "Some more text"),
        Em(text = " emphasized!"),
        NewLine(),
        Span(text = "And the last line")
      )
    ).render()

    for i <- 1 to 4 do
      Paragraph(text = s"i = $i").render()
      Thread.sleep(1000)
