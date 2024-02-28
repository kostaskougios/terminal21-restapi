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
        login <- new LoginPage().run()
        isYes <- new LoggedIn(login).run()
      yield isYes

      if confirmed.getOrElse(false) then println("User confirmed the details") else println("Not confirmed")

case class LoginForm(email: String = "my@email.com", pwd: String = "mysecret", submitted: Boolean = false, submittedInvalidEmail: Boolean = false):
  def isValidEmail: Boolean = email.contains("@")

/** The login form. Displays an email and password input and a submit button. When run() it will fill in the Login(email,pwd) model.
  */
class LoginPage(using session: ConnectedSession):
  private given initialModel: Model[LoginForm] = Model(LoginForm())
  val okIcon                                   = CheckCircleIcon(color = Some("green"))
  val notOkIcon                                = WarningTwoIcon(color = Some("red"))
  val emailInput                               = Input(key = "email", `type` = "email", defaultValue = initialModel.value.email)
    .onChange: changeEvent =>
      import changeEvent.*
      handled.withModel(model.copy(email = newValue))

  val submitButton = Button(key = "submit", text = "Submit")
    .onClick: clickEvent =>
      import clickEvent.*
      // if the email is invalid, we will not terminate. We also will render an error that will be visible for 2 seconds
      val isValidEmail = model.isValidEmail
      handled.withModel(_.copy(submitted = isValidEmail, submittedInvalidEmail = !isValidEmail))

  val passwordInput = Input(key = "password", `type` = "password", defaultValue = initialModel.value.pwd)
    .onChange: changeEvent =>
      import changeEvent.*
      handled.withModel(model.copy(pwd = newValue))

  val errorsBox            = Box()
  val errorMsgInvalidEmail = Paragraph(text = "Invalid Email", style = Map("color" -> "red"))

  def run(): Option[LoginForm] =
    controller
      .render()
      .handledEventsIterator
      .map(_.model)
      .tapEach: form =>
        println(form)
      .dropWhile(!_.submitted)
      .nextOption()

  def components(loginForm: LoginForm): Seq[UiElement] =
    Seq(
      QuickFormControl()
        .withLabel("Email address")
        .withHelperText("We'll never share your email.")
        .withInputGroup(
          InputLeftAddon().withChildren(EmailIcon()),
          emailInput,
          InputRightAddon().withChildren(if loginForm.isValidEmail then okIcon else notOkIcon)
        ),
      QuickFormControl()
        .withLabel("Password")
        .withHelperText("Don't share with anyone")
        .withInputGroup(
          InputLeftAddon().withChildren(ViewOffIcon()),
          passwordInput
        ),
      submitButton,
      if loginForm.submittedInvalidEmail then errorsBox.withChildren(errorMsgInvalidEmail) else errorsBox
    )

  def controller: Controller[LoginForm] = Controller(components)
    .onEvent: event =>
      import event.*
      val newModel = model.copy(submittedInvalidEmail = false)
      handled.withModel(newModel)

class LoggedIn(login: LoginForm)(using session: ConnectedSession):
  import Model.Standard.booleanFalseModel
  val yesButton = Button(key = "yes-button", text = "Yes")
    .onClick: e =>
      e.handled.withModel(true).terminate

  val noButton = Button(key = "no-button", text = "No")
    .onClick: e =>
      e.handled.withModel(false).terminate

  val emailDetails    = Text(text = s"email : ${login.email}")
  val passwordDetails = Text(text = s"password : ${login.pwd}")

  def run(): Option[Boolean] =
    controller.render().handledEventsIterator.lastOption.map(_.model)

  def components(m: Boolean): Seq[UiElement] =
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
  def controller: Controller[Boolean] = Controller(components)
