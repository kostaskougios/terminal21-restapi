package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Overlay:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val box1 = Box(text = "Clicks will be reported here.")
    Seq(
      commonBox(text = "Menus box0001"),
      HStack().withChildren(
        Menu().withChildren(
          MenuButton(text = "Actions menu0001", size = Some("sm"), colorScheme = Some("teal")).withChildren(
            ChevronDownIcon()
          ),
          MenuList().withChildren(
            MenuItem(text = "Download menu-download")
              .onClick: () =>
                box1.withText("'Download' clicked").renderChanges(),
            MenuItem(text = "Copy").onClick: () =>
              box1.withText("'Copy' clicked").renderChanges(),
            MenuItem(text = "Paste").onClick: () =>
              box1.withText("'Paste' clicked").renderChanges(),
            MenuDivider(),
            MenuItem(text = "Exit").onClick: () =>
              box1.withText("'Exit' clicked").renderChanges()
          )
        ),
        box1
      )
    )
