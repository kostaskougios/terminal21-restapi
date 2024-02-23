package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.commonBox

object Overlay:
  def components(using Model[Boolean]): Seq[UiElement] =
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
              .onClick: event =>
                import event.*
                handled.withRenderChanges(box1.withText("'Download' clicked"))
            ,
            MenuItem(text = "Copy").onClick: event =>
              import event.*
              handled.withRenderChanges(box1.withText("'Copy' clicked"))
            ,
            MenuItem(text = "Paste").onClick: event =>
              import event.*
              handled.withRenderChanges(box1.withText("'Paste' clicked"))
            ,
            MenuDivider(),
            MenuItem(text = "Exit").onClick: event =>
              import event.*
              handled.withRenderChanges(box1.withText("'Exit' clicked"))
          )
        ),
        box1
      )
    )
