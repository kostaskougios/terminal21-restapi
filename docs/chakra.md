# Chakra components

[The case classes](../terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala)

Note: only a fraction of the available properties of each component is documented here.
See the case class for a full list as well as the chakra react documentation (links are available in the scaladocs of each case class).
### Button

![Button](images/chakra/button.png)
```scala
val b = Button(text = "Keep Running")
b.onClick: () =>
    b.text = "Clicked"
    session.render()
```

### Box

![Box](images/chakra/box.png)
```scala
Box(text = "Badges", bg = "green", p = 4, color = "black")
```

## HStack / VStack
Horizontal / Vertical stack of elements

![HStack](images/chakra/hstack.png)
```scala
HStack().withChildren(
  checkbox1,
  checkbox2
)
```

### Menus

![Menu](images/chakra/menu.png)

```scala
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
  )
)
```