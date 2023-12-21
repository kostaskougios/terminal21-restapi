package tests

import org.terminal21.client.*
import org.terminal21.client.components.{Paragraph, render}
import tests.chakra.*

import java.util.concurrent.{CountDownLatch, TimeUnit}

@main def chakraComponents(): Unit =
  Sessions.withNewSession("chakra-components", "Chakra Components"): session =>
    given ConnectedSession = session

    val latch = new CountDownLatch(1)

    (Forms.components ++ Editables.components ++ Stacks.components ++ Grids.components ++ Buttons.components(
      latch
    ) ++ Etc.components ++ Icons.components ++ DataDisplay.components)
      .render()

    println("Waiting for button to be pressed for 1 hour")
    latch.await(1, TimeUnit.HOURS)
    session.clear()
    Paragraph(text = "Terminated").render()
