package tests

import org.terminal21.client.{ConnectedSession, Controller, Sessions}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

@main def loginForm(): Unit =
  Sessions
    .withNewSession("login-form", "Login Form")
    .connect: session =>
      given ConnectedSession = session

      val initialModel = Login("my@email.com", "mysecret")

      val emailInput      = Input(`type` = "email", defaultValue = initialModel.email)
      val submitButton    = Button(text = "Submit")
      val passwordInput   = Input(`type` = "password", defaultValue = initialModel.pwd)
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
        QuickFormControl()
          .withLabel("Password")
          .withHelperText("Don't share with anyone")
          .withInputGroup(
            InputLeftAddon().withChildren(ViewOffIcon()),
            passwordInput
          ),
        submitButton
      ).render()

      def validate(login: Login): InputRightAddon =
        if login.isValidEmail then emailRightAddon.withChildren(okIcon) else emailRightAddon.withChildren(notOkIcon)

      Controller(initialModel)
        .onEvent: model =>
          model.copy(email = emailInput.current.value, pwd = passwordInput.current.value)
        .onClick(submitButton): clickEvent =>
          clickEvent.handled.withShouldTerminate(clickEvent.model.isValidEmail)
        .onChange(emailInput): changeEvent =>
          changeEvent.handled.withRenderChanges(validate(changeEvent.model))
        .lastModelOption match
        case Some(login) if !session.isClosed => println(s"Login will be processed: $login")
        case _                                => println("Login cancelled")

private case class Login(email: String, pwd: String):
  def isValidEmail: Boolean = email.contains("@")
