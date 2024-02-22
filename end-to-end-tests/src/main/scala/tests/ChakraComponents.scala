package tests

import org.terminal21.client.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.render
import org.terminal21.client.components.std.Paragraph
import tests.chakra.*

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean

@main def chakraComponents(): Unit =
  val keepRunning = new AtomicBoolean(true)

  while keepRunning.get() do
    println("Starting new session")
    Sessions
      .withNewSession("chakra-components", "Chakra Components")
      .connect: session =>
        keepRunning.set(false)
        given ConnectedSession                = session
        given controller: Controller[Boolean] = Controller(false)

        // react tests reset the session to clear state
        val krButton = Button(text = "Reset state").onClick: event =>
          keepRunning.set(true)
          event.handled.terminate

        (Overlay.components ++ Forms.components ++ Editables.components ++ Stacks.components ++ Grids.components ++ Buttons.components(
          latch
        ) ++ Etc.components ++ MediaAndIcons.components ++ DataDisplay.components ++ Typography.components ++ Feedback.components ++ Disclosure.components ++ Navigation.components ++ Seq(
          krButton
        ))
          .render()

        println("Waiting for button to be pressed for 1 hour")
        session.waitTillUserClosesSessionOr(latch.getCount == 0)
        if !session.isClosed then
          session.clear()
          Paragraph(text = "Terminated").render()
          Thread.sleep(1000)
