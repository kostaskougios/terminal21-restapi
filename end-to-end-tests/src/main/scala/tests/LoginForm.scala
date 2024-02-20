package tests

import org.terminal21.client.{ConnectedSession, Controller, Sessions}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

@main def loginFormApp(): Unit =
  Sessions
    .withNewSession("login-form", "Login Form")
    .connect: session =>
      given ConnectedSession = session
      val form               = new LoginForm()
      form.run() match
        case Some(login) if !session.isClosed => println(s"Login will be processed: $login")
        case _                                => println("Login cancelled")

class LoginForm(using session: ConnectedSession):
  private val initialModel    = Login("my@email.com", "mysecret")
  private val okIcon          = CheckCircleIcon(color = Some("green"))
  private val notOkIcon       = WarningTwoIcon(color = Some("red"))
  private val emailRightAddon = InputRightAddon().withChildren(okIcon)
  private val emailInput      = Input(`type` = "email", defaultValue = initialModel.email)
  private val submitButton    = Button(text = "Submit")
  private val passwordInput   = Input(`type` = "password", defaultValue = initialModel.pwd)

  def run(): Option[Login] =
    components.render()
    processEvents

  def components: Seq[UiElement] =
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
    )

  def processEvents: Option[Login] = registerHandlers(Controller(initialModel)).lastModelOption

  def registerHandlers(controller: Controller[Login]): Controller[Login] =
    controller
      .onEvent: event =>
        val newModel = event.model.copy(email = emailInput.current.value, pwd = passwordInput.current.value)
        event.handled.withModel(newModel)
      .onClick(submitButton): clickEvent =>
        clickEvent.handled.withShouldTerminate(clickEvent.model.isValidEmail)
      .onChange(emailInput): changeEvent =>
        changeEvent.handled.withRenderChanges(validate(changeEvent.model))

  private def validate(login: Login): InputRightAddon =
    if login.isValidEmail then emailRightAddon.withChildren(okIcon) else emailRightAddon.withChildren(notOkIcon)

private case class Login(email: String, pwd: String):
  def isValidEmail: Boolean = email.contains("@")
