package tests.chakra

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*

object Typography:
  def components: Seq[UiElement] =
    Seq(
      Text(text = "typography-text-0001", color = Some("tomato"))
    )
