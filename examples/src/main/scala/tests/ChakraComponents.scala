package tests

import org.terminal21.client.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.{Paragraph, render}
import tests.chakra.*

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}

@main def chakraComponents(): Unit =
  val keepRunning = new AtomicBoolean(true)

  while keepRunning.get() do
    println("Starting new session")
    Sessions.withNewSession("chakra-components", "Chakra Components"): session =>
      keepRunning.set(false)
      given ConnectedSession = session

      val latch = new CountDownLatch(1)

      // react tests reset the session to clear state
      val krButton = Button(text = "Keep Running").onClick: () =>
        keepRunning.set(true)
        latch.countDown()

      (Overlay.components ++ Forms.components ++ Editables.components ++ Stacks.components ++ Grids.components ++ Buttons.components(
        latch
      ) ++ Etc.components ++ MediaAndIcons.components ++ DataDisplay.components ++ Seq(krButton))
        .render()

      println("Waiting for button to be pressed for 1 hour")
      latch.await(1, TimeUnit.HOURS)
      session.clear()
      Paragraph(text = "Terminated").render()
      Thread.sleep(2000)
