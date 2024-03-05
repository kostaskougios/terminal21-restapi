package tests.chakra

import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.*
import tests.chakra.Common.*

import java.util.concurrent.CountDownLatch

object Buttons:
  def components(m: ChakraModel, events: Events): MV[ChakraModel] =
    val box1       = commonBox(text = "Buttons")
    val exitButton = Button(key = "exit-button", text = "Click to exit program", colorScheme = Some("red"))
    val model      = m.copy(
      terminate = events.isClicked(exitButton)
    )
    MV(
      model,
      Seq(
        box1,
        exitButton
      )
    )
