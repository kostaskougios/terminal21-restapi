package tests

import org.terminal21.client.*
import org.terminal21.client.components.*

@main def stdComponents(): Unit =
  Sessions.withNewSession("std-components", "Std Components"): session =>
    given ConnectedSession = session

    val input  = Input(defaultValue = "Please enter your name")
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

    for i <- 1 to 400 do
      println(s"i = $i, input value = ${input.value}")
      Thread.sleep(1000)
