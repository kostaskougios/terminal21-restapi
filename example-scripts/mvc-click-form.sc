#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// MVC demo that handles a button click
// Run with ./mvc-click-form.sc
// ------------------------------------------------------------------------------

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.SessionOptions

Sessions
  .withNewSession("mvc-click-form", "MVC form with a button")
  .connect: session =>
    given ConnectedSession = session
    new ClickPage(ClickForm(false)).run() match
      case None        => // the user closed the app
      case Some(model) => println(s"model = $model")

    Thread.sleep(1000) // wait a bit so that the user can see the change in the UI

/** Our model
  *
  * @param clicked
  *   will be set to true when the button is clicked
  */
case class ClickForm(clicked: Boolean)

/** One nice way to structure the code (that simplifies testing too) is to create a class for every page in the user interface. In this instance, we create a
  * page for the click form to be displayed. All components are in `components` method. The controller is in the `controller` method and we can run to get the
  * result in the `run` method. We can use these methods in unit tests to test what is rendered and how events are processed respectively.
  */
class ClickPage(initialForm: ClickForm)(using ConnectedSession):
  def run(): Option[ClickForm] = controller.render(initialForm).run()

  def components(form: ClickForm, events: Events): MV[ClickForm] =
    val button = Button(key = "click-me", text = "Please click me")
    val updatedForm = form.copy(
      clicked = events.isClicked(button)
    )
    val msg = Paragraph(text = if updatedForm.clicked then "Button clicked!" else "Waiting for user to click the button")

    MV(
      updatedForm,
      Seq(msg, button),
      terminate = updatedForm.clicked // terminate the event iteration
    )

  def controller: Controller[ClickForm] = Controller(components)
