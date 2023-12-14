package tests

import org.terminal21.client.*
import org.terminal21.client.components.*

@main def stdComponents(): Unit =
  Sessions.withNewSession("std-components", "Std Components"): session =>
    given ConnectedSession = session

    val input = Input(defaultValue = "Please enter your name")
    input.onChange: newValue =>
      println(s"input new value = $newValue")
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
      )
    ).render()

    for i <- 1 to 400 do
      println(s"i = $i, input value = ${input.value}")
      Thread.sleep(1000)
