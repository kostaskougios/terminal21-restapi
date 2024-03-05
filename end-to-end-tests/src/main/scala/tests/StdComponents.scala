package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*

@main def stdComponents(): Unit =
  Sessions
    .withNewSession("std-components", "Std Components")
    .connect: session =>
      given ConnectedSession = session

      def components(events: Events) =
        val input        = Input(key = "name", defaultValue = "Please enter your name")
        val cookieReader = CookieReader(key = "cookie-reader", name = "std-components-test-cookie")

        val outputMsg = events.changedValue(input, "This will reflect what you type in the input")
        val output    = Paragraph(text = outputMsg)

        val cookieMsg   = events.changedValue(cookieReader).map(newValue => s"Cookie value $newValue").getOrElse("This will display the value of the cookie")
        val cookieValue = Paragraph(text = cookieMsg)

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
          Paragraph(text = "A Form").withChildren(input),
          output,
          Cookie(name = "std-components-test-cookie", value = "test-cookie-value"),
          cookieReader,
          cookieValue
        )

      Controller.noModel(components).render(()).iterator.lastOption
