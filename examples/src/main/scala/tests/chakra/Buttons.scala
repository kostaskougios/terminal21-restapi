package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, Button}
import tests.chakra.Common.*

import java.util.concurrent.CountDownLatch

object Buttons:
  def components(latch: CountDownLatch)(using session: ConnectedSession): Seq[UiElement] =
    val box1       = commonBox(text = "Buttons")
    val exitButton = Button(text = "Click to exit program", colorScheme = Some("red"))
    Seq(
      box1,
      exitButton.onClick: () =>
        box1.text = "Exit Clicked!"
        exitButton.text = "Stopping..."
        exitButton.colorScheme = Some("green")
        session.render()
        Thread.sleep(1000)
        latch.countDown()
    )
