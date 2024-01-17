package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.{Em, Header1, Input, NewLine, Paragraph, Span}

@main def stdComponents(): Unit =
  Sessions.withNewSession("std-components", "Std Components"): session =>
    given ConnectedSession = session

    val input  = Input(defaultValue = Some("Please enter your name"))
    val output = Paragraph(text = "This will reflect what you type in the input")
    input.onChange: newValue =>
      output.text = newValue
      session.render()

    Seq(
      Header1(text = "Welcome to the std components demo/test"),
      Paragraph(text = "Hello World!").withChildren(
        NewLine(),
        Span(text = "Some more text"),
        Em(text = " emphasized!"),
        NewLine(),
        Span(text = "And the last line")
      ),
      Paragraph(text = "A Form ").withChildren(
        input
      ),
      output
    ).render()

    session.waitTillUserClosesSession()
