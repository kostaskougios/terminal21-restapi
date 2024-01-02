package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Overlay:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val box1 = Box(text = "Clicks will be reported here.")
    Seq(
      commonBox(text = "Menus"),
      HStack().withChildren(
        Menu().withChildren(
          MenuButton(text = "Actions menu0001", size = Some("sm"), colorScheme = Some("teal")).withChildren(
            ChevronDownIcon()
          ),
          MenuList().withChildren(
            MenuItem(text = "Download menu-download")
              .onClick: () =>
                box1.text = "'Download' clicked"
                session.render()
            ,
            MenuItem(text = "Copy").onClick: () =>
              box1.text = "'Copy' clicked"
              session.render()
            ,
            MenuItem(text = "Paste").onClick: () =>
              box1.text = "'Paste' clicked"
              session.render()
            ,
            MenuDivider(),
            MenuItem(text = "Exit").onClick: () =>
              box1.text = "'Exit' clicked"
              session.render()
          )
        ),
        box1
      )
    )
