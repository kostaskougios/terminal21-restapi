package tests.chakra

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, HStack, VStack}
import tests.chakra.Common.*

object Stacks:
  def components: Seq[UiElement] =
    Seq(
      commonBox(text = "VStack"),
      VStack(spacing = Some("24px"), align = Some("stretch")).withChildren(
        Box(text = "1", bg = "green", p = 2, color = "black"),
        Box(text = "2", bg = "red", p = 2, color = "black"),
        Box(text = "3", bg = "blue", p = 2, color = "black")
      ),
      commonBox(text = "HStack"),
      HStack(spacing = Some("24px")).withChildren(
        Box(text = "1", bg = "green", p = 2, color = "black"),
        Box(text = "2", bg = "red", p = 2, color = "black"),
        Box(text = "3", bg = "blue", p = 2, color = "black")
      )
    )
