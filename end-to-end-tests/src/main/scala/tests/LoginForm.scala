package tests

import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.{NewLine, Paragraph}
import org.terminal21.client.*

@main def loginFormApp(): Unit =
  Sessions
    .withNewSession("login-form", "Login Form")
    .connect: session =>
      given ConnectedSession = session
      val confirmed          = for
        login <- new LoginForm().run()
        isYes <- new LoggedIn(login).run()
      yield isYes

      if confirmed.getOrElse(false) then println("User confirmed the details") else println("Not confirmed")

case class Login(email: String, pwd: String):
  def isValidEmail: Boolean = email.contains("@")

/** The login form. Displays an email and password input and a submit button. When run() it will fill in the Login(email,pwd) model.
  */
class LoginForm(using session: ConnectedSession):
  private given initialModel: Model[Login] = Model(Login("my@email.com", "mysecret"))
  val okIcon                               = CheckCircleIcon(color = Some("green"))
  val notOkIcon                            = WarningTwoIcon(color = Some("red"))
  val emailRightAddon                      = InputRightAddon().withChildren(okIcon)
  val emailInput                           = Input(`type` = "email", defaultValue = initialModel.value.email)
    .onChange: changeEvent =>
      changeEvent.handled.withRenderChanges(validate(changeEvent.model))

  val submitButton = Button(text = "Submit")
    .onClick: clickEvent =>
      import clickEvent.*
      // if the email is invalid, we will not terminate. We also will render an error that will be visible for 2 seconds
      val isValidEmail = clickEvent.model.isValidEmail
      val messageBox   =
        if isValidEmail then errorsBox.current else errorsBox.current.addChildren(errorMsgInvalidEmail)
      clickEvent.handled.withShouldTerminate(isValidEmail).withRenderChanges(messageBox).addTimedRenderChange(2000, errorsBox)

  val passwordInput        = Input(`type` = "password", defaultValue = initialModel.value.pwd)
  val errorsBox            = Box()
  val errorMsgInvalidEmail = Paragraph(text = "Invalid Email", style = Map("color" -> "red"))

  def run(): Option[Login] =
    controller.render().handledEventsIterator.lastOptionOrNoneIfSessionClosed.map(_.model)

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

  def controller: Controller[Login] = Controller(components)
    .onEvent: event =>
      import event.*
      val newModel = event.model.copy(email = emailInput.current.value, pwd = passwordInput.current.value)
      event.handled.withModel(newModel)

  private def validate(login: Login): InputRightAddon =
    if login.isValidEmail then emailRightAddon.withChildren(okIcon) else emailRightAddon.withChildren(notOkIcon)

class LoggedIn(login: Login)(using session: ConnectedSession):
  private given Model[Boolean] = Model(false)
  val yesButton                = Button(text = "Yes")
    .onClick: e =>
      e.handled.withModel(true).terminate

  val noButton = Button(text = "No")
    .onClick: e =>
      e.handled.withModel(false).terminate

  val emailDetails    = Text(text = s"email : ${login.email}")
  val passwordDetails = Text(text = s"password : ${login.pwd}")

  def run(): Option[Boolean] =
    controller.render().handledEventsIterator.lastOption.map(_.model)

  def components =
    Seq(
      Paragraph().withChildren(
        Text(text = "Are your details correct?"),
        NewLine(),
        emailDetails,
        NewLine(),
        passwordDetails
      ),
      HStack().withChildren(yesButton, noButton)
    )

  /** @return
    *   A controller with a boolean value, true if user clicked "Yes", false for "No"
    */
  def controller = Controller(components)
