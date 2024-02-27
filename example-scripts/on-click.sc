#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.SessionOptions

case class ClickForm(clicked: Boolean)

Sessions
  .withNewSession("on-click-example", "On Click Handler")
  .connect: session =>
    given ConnectedSession = session
    new ClickPage(ClickForm(false)).run match
      case None        => // the user closed the app
      case Some(model) => println(s"model = $model")

    session.leaveSessionOpenAfterExiting() // leave the session open after exiting so that the user can examine the UI

/** One nice way to structure the code (that simplifies testing too) is to create a class for every page in the user interface. In this instance, we create a
  * page for the click form to be displayed. All components are in `components` method. The controller is in the `controller` method and we can run to get the
  * result in the `run` method. We can use these methods in unit tests to test what is rendered and how events are processed respectively.
  */
class ClickPage(clickForm: ClickForm)(using ConnectedSession):
  given Model[ClickForm] = Model(clickForm)

  def run = controller.render().handledEventsIterator.lastOption.map(_.model)

  def components: Seq[UiElement] =
    val msg = Paragraph(text = "Waiting for user to click the button")
    val button = Button(text = "Please click me").onClick: event =>
      import event.*
      handled.withModel(model.copy(clicked = true)).withRenderChanges(msg.withText("Button clicked")).terminate
    Seq(msg, button)

  def controller: Controller[ClickForm] = Controller(components)
