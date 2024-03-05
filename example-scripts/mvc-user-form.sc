#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.components.chakra.*

case class UserForm(
    email: String, // the email
    submitted: Boolean // true if user clicks the submit button, false otherwise
)

Sessions
  .withNewSession("mvc-user-form", "MVC example with a user form")
  .connect: session =>
    given ConnectedSession = session
    new UserPage(UserForm("my@email.com", false)).run match
      case Some(submittedUser) =>
        println(s"Submitted: $submittedUser")
      case None =>
        println("User closed session without submitting the form")

/** One nice way to structure the code (that simplifies testing too) is to create a class for every page in the user interface. In this instance, we create a
  * page for the user form to be displayed. All components are in `components` method. The controller is in the `controller` method and we can run to get the
  * result in the `run` method. We can use these methods in unit tests to test what is rendered and how events are processed respectively.
  */
class UserPage(initialForm: UserForm)(using ConnectedSession):

  /** Runs the form and returns the results
    * @return
    *   if None, the user didn't submit the form (i.e. closed the session), if Some(userForm) the user submitted the form.
    */
  def run: Option[UserForm] =
    controller.render(initialForm).iterator.lastOption.map(_.model).filter(_.submitted)

  /** @return
    *   all the components that should be rendered for the page
    */
  def components(form: UserForm, events: Events): MV[UserForm] =
    val email = Input(key = "email", `type` = "email", defaultValue = initialForm.email)
    val submit = Button(key = "submit", text = "Submit")

    val updatedForm = form.copy(
      email = events.changedValue(email, form.email),
      submitted = events.isClicked(submit)
    )

    val output = Paragraph(text = if events.isChangedValue(email) then s"Email changed: ${updatedForm.email}" else "Please modify the email.")

    MV(
      updatedForm,
      Seq(
        QuickFormControl()
          .withLabel("Email address")
          .withInputGroup(
            InputLeftAddon().withChildren(EmailIcon()),
            email
          )
          .withHelperText("We'll never share your email."),
        submit,
        output
      ),
      terminate = events.isClicked(submit)
    )

  def controller: Controller[UserForm] = Controller(components)
