package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*

@main def stdComponents(): Unit =
  Sessions
    .withNewSession("std-components", "Std Components")
    .connect: session =>
      given ConnectedSession = session
      import Model.unitModel

      val output      = Paragraph(text = "This will reflect what you type in the input")
      val cookieValue = Paragraph(text = "This will display the value of the cookie")
      val input       = Input(defaultValue = "Please enter your name").onChange: event =>
        import event.*
        handled.withRenderChanges(output.withText(newValue))

      val components = Seq(
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
        Paragraph(text = "A Form").withChildren(
          input
        ),
        output,
        Cookie(name = "std-components-test-cookie", value = "test-cookie-value"),
        CookieReader(name = "std-components-test-cookie").onChange: event =>
          import event.*
          handled.withRenderChanges(cookieValue.withText(s"Cookie value $newValue"))
        ,
        cookieValue
      )

      Controller(components).eventsIterator.lastOption
