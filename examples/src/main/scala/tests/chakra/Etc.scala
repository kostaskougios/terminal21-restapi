package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Etc:
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      commonBox(text = "Center"),
      Center(text = "Center demo, not styled"),
      Center(text = "Center demo", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px")),
      commonBox(text = "Circle"),
      Circle(text = "Circle demo, not styled"),
      Circle(text = "Circle demo", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px")),
      commonBox(text = "Square"),
      Square(text = "Square demo, not styled"),
      Square(text = "Square demo", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px"))
    )
