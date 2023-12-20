package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, ChakraProps, HStack, VStack}
import tests.chakra.Common.greenProps

object Stacks:
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      Box(text = "VStack", props = greenProps),
      VStack(spacing = Some("24px")).withChildren(
        Box(text = "1", props = ChakraProps(bg = "green", p = 2, color = "black")),
        Box(text = "2", props = ChakraProps(bg = "red", p = 2, color = "black")),
        Box(text = "3", props = ChakraProps(bg = "blue", p = 2, color = "black"))
      ),
      Box(text = "HStack", props = greenProps),
      HStack(spacing = Some("24px")).withChildren(
        Box(text = "1", props = ChakraProps(bg = "green", p = 2, color = "black")),
        Box(text = "2", props = ChakraProps(bg = "red", p = 2, color = "black")),
        Box(text = "3", props = ChakraProps(bg = "blue", p = 2, color = "black"))
      )
    )
