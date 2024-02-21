package tests

import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.{ConnectedSession, Controller, Sessions}

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
  private val initialModel = Login("my@email.com", "mysecret")
  val okIcon               = CheckCircleIcon(color = Some("green"))
  val notOkIcon            = WarningTwoIcon(color = Some("red"))
  val emailRightAddon      = InputRightAddon().withChildren(okIcon)
  val emailInput           = Input(`type` = "email", defaultValue = initialModel.email)
  val submitButton         = Button(text = "Submit")
  val passwordInput        = Input(`type` = "password", defaultValue = initialModel.pwd)
  val errorsBox            = Box()
  val errorMsgInvalidEmail = Paragraph(text = "Invalid Email", style = Map("color" -> "red"))

  def run(): Option[Login] =
    components.render()
    controller.eventsIterator.lastOption

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
      submitButton,
      errorsBox
    )

  def controller: Controller[Login] = Controller(initialModel)
    .onEvent: event =>
      val newModel = event.model.copy(email = emailInput.current.value, pwd = passwordInput.current.value)
      event.handled.withModel(newModel)
    .onClick(submitButton): clickEvent =>
      // if the email is invalid, we will not terminate. We also will render an error that will be visible for 2 seconds
      val isValidEmail = clickEvent.model.isValidEmail
      val messageBox   =
        if isValidEmail then errorsBox.current else errorsBox.current.addChildren(errorMsgInvalidEmail)
      clickEvent.handled.withShouldTerminate(isValidEmail).withRenderChanges(messageBox).addTimedRenderChange(2000, errorsBox)
    .onChange(emailInput): changeEvent =>
      changeEvent.handled.withRenderChanges(validate(changeEvent.model))

  private def validate(login: Login): InputRightAddon =
    if login.isValidEmail then emailRightAddon.withChildren(okIcon) else emailRightAddon.withChildren(notOkIcon)

private case class Login(email: String, pwd: String):
  def isValidEmail: Boolean = email.contains("@")
