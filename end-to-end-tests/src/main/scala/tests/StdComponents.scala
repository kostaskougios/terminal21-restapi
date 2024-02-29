package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*

@main def stdComponents(): Unit =
  Sessions
    .withNewSession("std-components", "Std Components")
    .connect: session =>
      given ConnectedSession = session

      case class Form(output: String, cookie: String)
      given model: Model[Form] = Model(Form("This will reflect what you type in the input", "This will display the value of the cookie"))

      def components(form: Form) =
        val output      = Paragraph(text = form.output)
        val cookieValue = Paragraph(text = form.cookie)
        val input       = Input(key = "name", defaultValue = "Please enter your name").onChange: event =>
          import event.*
          handled.withModel(form.copy(output = newValue))

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
          CookieReader(key = "cookie-reader", name = "std-components-test-cookie").onChange: event =>
            import event.*
            handled.withModel(_.copy(cookie = s"Cookie value $newValue"))
          ,
          cookieValue
        )

      Controller(components(model.value)).render().handledEventsIterator.lastOption
