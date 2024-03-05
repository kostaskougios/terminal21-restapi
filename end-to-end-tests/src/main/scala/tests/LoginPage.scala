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
  private val initialModel = LoginForm()
  val okIcon               = CheckCircleIcon(color = Some("green"))
  val notOkIcon            = WarningTwoIcon(color = Some("red"))
  val emailInput           = Input(key = "email", `type` = "email", defaultValue = initialModel.email)

  val submitButton = Button(key = "submit", text = "Submit")

  val passwordInput = Input(key = "password", `type` = "password", defaultValue = initialModel.pwd)

  val errorsBox            = Box()
  val errorMsgInvalidEmail = Paragraph(text = "Invalid Email", style = Map("color" -> "red"))

  def run(): Option[LoginForm] =
    controller
      .render(initialModel)
      .iterator
      .map(_.model)
      .tapEach: form =>
        println(form)
      .dropWhile(!_.submitted)
      .nextOption()

  def components(form: LoginForm, events: Events): MV[LoginForm] =
    println(events.event)
    val isValidEmail = form.isValidEmail
    val newForm      = form.copy(
      email = events.changedValue(emailInput, form.email),
      pwd = events.changedValue(passwordInput, form.pwd),
      submitted = events.isClicked(submitButton) && isValidEmail,
      submittedInvalidEmail = events.isClicked(submitButton) && !isValidEmail
    )
    val view         = Seq(
      QuickFormControl()
        .withLabel("Email address")
        .withHelperText("We'll never share your email.")
        .withInputGroup(
          InputLeftAddon().withChildren(EmailIcon()),
          emailInput,
          InputRightAddon().withChildren(if newForm.isValidEmail then okIcon else notOkIcon)
        ),
      QuickFormControl()
        .withLabel("Password")
        .withHelperText("Don't share with anyone")
        .withInputGroup(
          InputLeftAddon().withChildren(ViewOffIcon()),
          passwordInput
        ),
      submitButton,
      errorsBox.withChildren(if newForm.submittedInvalidEmail then errorMsgInvalidEmail else errorsBox)
    )
    MV(
      newForm,
      view
    )

  def controller: Controller[LoginForm] = Controller(components)

class LoggedIn(login: LoginForm)(using session: ConnectedSession):
  val yesButton = Button(key = "yes-button", text = "Yes")

  val noButton = Button(key = "no-button", text = "No")

  val emailDetails    = Text(text = s"email : ${login.email}")
  val passwordDetails = Text(text = s"password : ${login.pwd}")

  def run(): Option[Boolean] =
    controller.render(false).iterator.lastOption.map(_.model)

  def components(isYes: Boolean, events: Events): MV[Boolean] =
    val view = Seq(
      Paragraph().withChildren(
        Text(text = "Are your details correct?"),
        NewLine(),
        emailDetails,
        NewLine(),
        passwordDetails
      ),
      HStack().withChildren(yesButton, noButton)
    )
    MV(
      events.isClicked(yesButton),
      view,
      events.isClicked(yesButton) || events.isClicked(noButton)
    )

  /** @return
    *   A controller with a boolean value, true if user clicked "Yes", false for "No"
    */
  def controller: Controller[Boolean] = Controller(components)
