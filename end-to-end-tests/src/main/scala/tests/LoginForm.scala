package tests

import org.terminal21.client.{ConnectedSession, Sessions}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

@main def loginForm(): Unit =
  Sessions
    .withNewSession("login-form", "Login Form")
    .connect: session =>
      given ConnectedSession = session

      val emailInput      = Input(`type` = "email", defaultValue = "my@email.com")
      val submitButton    = Button(text = "Submit")
      val passwordInput   = Input(`type` = "password", defaultValue = "mysecret")
      val okIcon          = CheckCircleIcon(color = Some("green"))
      val notOkIcon       = WarningTwoIcon(color = Some("red"))
      val emailRightAddon = InputRightAddon().withChildren(okIcon)
      Seq(
        QuickFormControl()
          .withLabel("Email address")
          .withHelperText("We'll never share your email.")
          .withInputGroup(
            InputLeftAddon().withChildren(EmailIcon()),
            emailInput,
            emailRightAddon
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

      def validate(p: PersonSubmitted): Unit =
        println(p)
        val emailAddon = if p.isValidEmail then emailRightAddon.withChildren(okIcon) else emailRightAddon.withChildren(notOkIcon)
        emailAddon.renderChanges()

      val p = session.eventIterator
        .map: e =>
          println(e)
          val email = emailInput.current.value
          val pwd   = passwordInput.current.value
          PersonSubmitted(email, email.contains("@"), pwd, e.isTarget(submitButton), e.isSessionClose)
        .tapEach(validate)
        .dropWhile(p => !(p.isSubmitted && p.isValidEmail) && !p.userClosedSession)
        .next()
      println("Result:" + p)
