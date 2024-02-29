package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Overlay:
  def components(m: ChakraModel)(using Model[ChakraModel]): Seq[UiElement] =
    val box1 = Box().onModelChange: (b, m) =>
      b.withText(m.box1)
    Seq(
      commonBox(text = "Menus box0001"),
      HStack().withChildren(
        Menu(key = "menu1").withChildren(
          MenuButton(text = "Actions menu0001", size = Some("sm"), colorScheme = Some("teal")).withChildren(
            ChevronDownIcon()
          ),
          MenuList().withChildren(
            MenuItem(key = "download-menu", text = "Download menu-download")
              .onClick: event =>
                import event.*
                handled.withModel(_.copy(box1 = "'Download' clicked"))
            ,
            MenuItem(key = "copy-menu", text = "Copy").onClick: event =>
              import event.*
              handled.withModel(_.copy(box1 = "'Copy' clicked"))
            ,
            MenuItem(key = "paste-menu", text = "Paste").onClick: event =>
              import event.*
              handled.withModel(_.copy(box1 = "'Paste' clicked"))
            ,
            MenuDivider(),
            MenuItem(key = "exit-menu", text = "Exit").onClick: event =>
              import event.*
              handled.withModel(_.copy(box1 = "'Exit' clicked"))
          )
        ),
        box1
      )
    )
