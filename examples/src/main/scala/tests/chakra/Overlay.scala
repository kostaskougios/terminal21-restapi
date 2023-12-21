package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Overlay:
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      commonBox(text = "Menus"),
      HStack().withChildren(
        Menu().withChildren(
          MenuButton(text = "Actions", size = Some("sm"), colorScheme = Some("teal")).withChildren(
            ArrowDownIcon()
          ),
          MenuList().withChildren(
            MenuItem(text = "Download"),
            MenuItem(text = "Copy"),
            MenuItem(text = "Paste")
          )
        )
      )
    )
