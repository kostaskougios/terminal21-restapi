package tests

import org.terminal21.client.{ConnectedSession, Sessions}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

@main def loginForm(): Unit =
  Sessions
    .withNewSession("std-components", "Std Components")
    .connect: session =>
      given ConnectedSession = session

      val email        = Input(`type` = "email", value = "my@email.com")
      val description  = Textarea(placeholder = "Please enter a few things about you")
      val submitButton = Button(text = "Submit")
      val password     = Input(`type` = "password", value = "mysecret")
      Seq(
        FormControl().withChildren(
          FormLabel(text = "Email address"),
          InputGroup().withChildren(
            InputLeftAddon().withChildren(EmailIcon()),
            email,
            InputRightAddon().withChildren(CheckCircleIcon(color = Some("green")))
          ),
          FormHelperText(text = "We'll never share your email.")
        ),
        FormControl().withChildren(
          FormLabel(text = "Description"),
          InputGroup().withChildren(
            InputLeftAddon().withChildren(EditIcon()),
            description
          ),
          FormHelperText(text = "We'll never share your email.")
        ),
        FormControl().withChildren(
          FormLabel(text = "Password"),
          InputGroup().withChildren(
            InputLeftAddon().withChildren(ViewOffIcon()),
            password
          ),
          FormHelperText(text = "Don't share with anyone")
        ),
        submitButton
      ).render()

      case class PersonSubmitted(email: String, pwd: String, isSubmitted: Boolean, userClosedSession: Boolean)
      val o = session.globalEventIterator
        .map: e =>
          PersonSubmitted(email.current.value, password.current.value, e.isReceivedBy(submitButton), e.isSessionClose)
        .dropWhile(p => !p.isSubmitted && !p.userClosedSession)
        .nextOption()
      println(o.mkString("\n"))
