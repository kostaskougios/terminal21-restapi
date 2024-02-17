package tests

import org.terminal21.client.{ConnectedSession, Sessions}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

@main def loginForm(): Unit =
  Sessions
    .withNewSession("std-components", "Std Components")
    .connect: session =>
      given ConnectedSession = session

      val emailInput      = Input(`type` = "email", value = "my@email.com")
      val submitButton    = Button(text = "Submit")
      val passwordInput   = Input(`type` = "password", value = "mysecret")
      val okIcon          = CheckCircleIcon(color = Some("green"))
      val notOkIcon       = WarningTwoIcon(color = Some("red"))
      val emailRightAddon = InputRightAddon().withChildren(okIcon)
      Seq(
        FormControl().withChildren(
          FormLabel(text = "Email address"),
          InputGroup().withChildren(
            InputLeftAddon().withChildren(EmailIcon()),
            emailInput,
            emailRightAddon
          ),
          FormHelperText(text = "We'll never share your email.")
        ),
        FormControl().withChildren(
          FormLabel(text = "Password"),
          InputGroup().withChildren(
            InputLeftAddon().withChildren(ViewOffIcon()),
            passwordInput
          ),
          FormHelperText(text = "Don't share with anyone")
        ),
        submitButton
      ).render()

      case class PersonSubmitted(email: String, isValidEmail: Boolean, pwd: String, isSubmitted: Boolean, userClosedSession: Boolean)
      val p = session.eventIterator
        .map: e =>
          val email = emailInput.current.value
          val pwd   = passwordInput.current.value
          PersonSubmitted(email, email.contains("@"), pwd, e.isTarget(submitButton), e.isSessionClose)
        .tapEach: p =>
          val emailAddon = if p.isValidEmail then emailRightAddon.withChildren(okIcon) else emailRightAddon.withChildren(notOkIcon)
          emailAddon.renderChanges()
        .dropWhile(p => !(p.isSubmitted && p.isValidEmail) && !p.userClosedSession)
        .next()
      println("Result:" + p)
