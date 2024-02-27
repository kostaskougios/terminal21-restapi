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
  .withNewSession("on-change-example", "On Change event handler")
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
class UserPage(user: UserForm)(using ConnectedSession):
  given Model[UserForm] = Model(user) // the Model for our page. This is given so that we can handle events and create the controller.

  /** Runs the form and returns the results
    * @return
    *   if None, the user didn't submit the form (i.e. closed the session), if Some(userForm) the user submitted the form.
    */
  def run: Option[UserForm] =
    controller.render().handledEventsIterator.lastOption.map(_.model).filter(_.submitted)

  /** @return
    *   all the components that should be rendered for the page
    */
  def components: Seq[UiElement] =
    val output = Paragraph(text = "Please modify the email.")
    val email = Input(`type` = "email", defaultValue = user.email).onChange: event =>
      import event.*
      val v = event.newValue
      handled.withModel(model.copy(email = v)).withRenderChanges(output.withText(s"Email value : $v"))

    Seq(
      QuickFormControl()
        .withLabel("Email address")
        .withInputGroup(
          InputLeftAddon().withChildren(EmailIcon()),
          email
        )
        .withHelperText("We'll never share your email."),
      Button(text = "Submit").onClick: event =>
        import event.*
        handled.withModel(model.copy(submitted = true)).terminate // mark the form as submitted and terminate the event iteration
      ,
      output
    )

  def controller: Controller[UserForm] = Controller(components)
