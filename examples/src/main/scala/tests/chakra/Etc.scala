package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.Center

object Etc:
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      Center(text = "Center demo, not styled"),
      Center(text = "Center demo", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px"))
    )
