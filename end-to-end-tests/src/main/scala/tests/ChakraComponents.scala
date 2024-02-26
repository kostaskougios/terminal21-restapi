package tests

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.*

import java.util.concurrent.atomic.AtomicBoolean

@main def chakraComponents(): Unit =
  val keepRunning = new AtomicBoolean(true)

  def loop(): Unit =
    println("Starting new session")
    Sessions
      .withNewSession("chakra-components", "Chakra Components")
      .connect: session =>
        keepRunning.set(false)
        given ConnectedSession      = session
        given model: Model[Boolean] = Model(false)

        // react tests reset the session to clear state
        val krButton = Button(text = "Reset state").onClick: event =>
          keepRunning.set(true)
          event.handled.terminate

        val components: Seq[UiElement] =
          Overlay.components ++ Forms.components ++ Editables.components ++ Stacks.components ++ Grids.components ++ Buttons.components ++ Etc.components ++ MediaAndIcons.components ++ DataDisplay.components ++ Typography.components ++ Feedback.components ++ Disclosure.components ++ Navigation.components ++ Seq(
            krButton
          )
        Controller(components).render().eventsIterator.lastOption match
          case Some(true) => loop()
          case _          =>

  loop()
