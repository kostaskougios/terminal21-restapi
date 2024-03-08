package tests.chakra

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Etc:
  def components: Seq[UiElement] =
    Seq(
      commonBox(text = "Center"),
      Center(text = "Center demo, not styled"),
      Center(text = "Center demo center-demo-0001", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px")),
      commonBox(text = "Circle"),
      Circle(text = "Circle demo, not styled"),
      Circle(text = "Circle demo circle-demo-0001", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px")),
      commonBox(text = "Square"),
      Square(text = "Square demo, not styled"),
      Square(text = "Square demo square-demo-0001", bg = Some("tomato"), color = Some("white"), w = Some("100px"), h = Some("100px"))
    )
