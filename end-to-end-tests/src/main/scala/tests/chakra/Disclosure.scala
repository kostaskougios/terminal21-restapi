package tests.chakra

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.Common.commonBox

object Disclosure:
  def components: Seq[UiElement] =
    Seq(
      commonBox(text = "Tabs"),
      Tabs().withChildren(
        TabList().withChildren(
          Tab(text = "tab-one").withSelected(Map("color" -> "white", "bg" -> "blue.500")),
          Tab(text = "tab-two").withSelected(Map("color" -> "white", "bg" -> "green.400")),
          Tab(text = "tab-three")
        ),
        TabPanels().withChildren(
          TabPanel().withChildren(
            Paragraph(text = "tab-1-content")
          ),
          TabPanel().withChildren(
            Paragraph(text = "tab-2-content")
          ),
          TabPanel().withChildren(
            Paragraph(text = "tab-3-content")
          )
        )
      )
    )
