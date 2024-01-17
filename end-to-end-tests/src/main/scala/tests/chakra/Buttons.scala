package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

import java.util.concurrent.CountDownLatch

object Buttons:
  def components(latch: CountDownLatch)(using session: ConnectedSession): Seq[UiElement] =
    val box1       = commonBox(text = "Buttons")
    val exitButton = Button(text = "Click to exit program", colorScheme = Some("red"))
    Seq(
      box1,
      exitButton.onClick: () =>
        Seq(
          box1.withText("Exit Clicked!"),
          exitButton.withText("Stopping...").withColorScheme(Some("green"))
        ).renderChanges()
        Thread.sleep(1000)
        latch.countDown()
    )
