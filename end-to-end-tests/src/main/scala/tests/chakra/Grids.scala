package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, SimpleGrid}
import tests.chakra.Common.*

object Grids:
  def components: Seq[UiElement] =
    val box1 = commonBox(text = "Simple grid")
    Seq(
      box1,
      SimpleGrid(spacing = Some("8px"), columns = 4).withChildren(
        Box(text = "One", bg = "yellow", color = "black"),
        Box(text = "Two", bg = "tomato", color = "black"),
        Box(text = "Three", bg = "blue", color = "black")
      )
    )
