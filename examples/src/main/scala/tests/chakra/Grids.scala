package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, ChakraProps, SimpleGrid}
import tests.chakra.Common.greenProps

object Grids :
  def components(using session: ConnectedSession): Seq[UiElement] =
    val box1 = Box(text = "Simple grid", props = greenProps)
    Seq(
      box1,
      SimpleGrid(spacing = Some("8px"), columns = 4).withChildren(
        Box(text = "One", props = ChakraProps(bg = "yellow", color = "black")),
        Box(text = "Two", props = ChakraProps(bg = "tomato", color = "black")),
        Box(text = "Three", props = ChakraProps(bg = "blue", color = "black"))
      )
    )

