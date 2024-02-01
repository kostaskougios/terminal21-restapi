package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*

@main def stdComponents(): Unit =
  Sessions.withNewSession("std-components", "Std Components"): session =>
    given ConnectedSession = session

    val input  = Input(defaultValue = Some("Please enter your name"))
    val output = Paragraph(text = "This will reflect what you type in the input")
    input.onChange: newValue =>
      output.withText(newValue).renderChanges()

    Seq(
      Header1(text = "header1 test"),
      Header2(text = "header2 test"),
      Header3(text = "header3 test"),
      Header4(text = "header4 test"),
      Header5(text = "header5 test"),
      Header6(text = "header6 test"),
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
