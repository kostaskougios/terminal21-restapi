#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.components.chakra.*

case class UserForm(email: String, submitted: Boolean)

Sessions
  .withNewSession("on-change-example", "On Change event handler")
  .connect: session =>
    given ConnectedSession = session
    val user = UserForm("my@email.com", false)
    given Model[UserForm] = Model(user)

    val output = Paragraph(text = "Please modify the email.")
    val email = Input(`type` = "email", defaultValue = user.email).onChange: event =>
      import event.*
      val v = event.newValue
      handled.withModel(model.copy(email = v)).withRenderChanges(output.withText(s"Email value : $v"))

    Controller(
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
    ).render().handledEventsIterator.lastOption.map(_.model).filter(_.submitted) match
      case Some(submittedUser) =>
        println(s"Submitted: $submittedUser")
      case None =>
        println("User closed session without submitting the form")
