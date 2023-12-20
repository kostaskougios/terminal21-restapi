package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.greenProps

object Icons:
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      Box(text = "Icons", props = greenProps),
      HStack().withChildren(
        InfoIcon(color = Some("tomato")),
        MoonIcon(color = Some("green"))
      )
    )
