package tests.chakra

import org.terminal21.client.{ConnectedSession, Model}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

import java.util.concurrent.CountDownLatch

object Buttons:
  def components(using Model[ChakraModel]): Seq[UiElement] =
    val box1       = commonBox(text = "Buttons")
    val exitButton = Button(key = "exit-button", text = "Click to exit program", colorScheme = Some("red")).onClick: event =>
      event.handled.terminate
    Seq(
      box1,
      exitButton
    )
