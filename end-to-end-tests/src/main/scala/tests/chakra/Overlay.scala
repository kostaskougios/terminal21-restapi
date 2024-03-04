package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Overlay:
  def components(events: Events): Seq[UiElement] =
    val mi1 = MenuItem(key = "download-menu", text = "Download menu-download")
    val mi2 = MenuItem(key = "copy-menu", text = "Copy")
    val mi3 = MenuItem(key = "paste-menu", text = "Paste")
    val mi4 = MenuItem(key = "exit-menu", text = "Exit")

    val box1Msg =
      if events.isClicked(mi1) then "'Download' clicked"
      else if events.isClicked(mi2) then "'Copy' clicked"
      else if events.isClicked(mi3) then "'Paste' clicked"
      else if events.isClicked(mi4) then "'Exit' clicked"
      else "Clicks will be reported here."

    val box1 = Box(text = box1Msg)

    Seq(
      commonBox(text = "Menus box0001"),
      HStack().withChildren(
        Menu(key = "menu1").withChildren(
          MenuButton(text = "Actions menu0001", size = Some("sm"), colorScheme = Some("teal")).withChildren(
            ChevronDownIcon()
          ),
          MenuList().withChildren(
            mi1,
            mi2,
            mi3,
            MenuDivider(),
            mi4
          )
        ),
        box1
      )
    )
