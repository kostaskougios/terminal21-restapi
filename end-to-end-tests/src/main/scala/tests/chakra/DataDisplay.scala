package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.NewLine
import tests.chakra.Common.*

object DataDisplay:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val headAndFoot = Tr().withChildren(
      Th(text = "To convert"),
      Th(text = "into"),
      Th(text = "multiply by", isNumeric = true)
    )
    val quickTable1 = QuickTable()
      .withHeaders("id", "name")
      .caption("Quick Table Caption")
      .withRows(
        Seq(
          Seq(1, "Kostas"),
          Seq(2, "Andreas")
        )
      )
    Seq(
      commonBox(text = "Badges"),
      HStack().withChildren(
        Badge(text = "badge 1", size = "sm"),
        Badge(text = "badge 2", size = "md", colorScheme = Some("red")),
        Badge(text = "badge 3", size = "lg", colorScheme = Some("green")),
        Badge(text = "badge 4", variant = Some("outline"), colorScheme = Some("tomato")),
        Badge(text = "badge 4").withChildren(
          Button(text = "test")
        )
      ),
      commonBox(text = "Quick Tables"),
      quickTable1,
      commonBox(text = "Tables"),
      TableContainer().withChildren(
        Table(variant = "striped", colorScheme = Some("teal"), size = "lg").withChildren(
          TableCaption(text = "Imperial to metric conversion factors (table-caption-0001)"),
          Thead().withChildren(
            headAndFoot
          ),
          Tbody().withChildren(
            Tr().withChildren(
              Td(text = "inches"),
              Td(text = "millimetres (mm)"),
              Td(text = "25.4", isNumeric = true)
            ),
            Tr().withChildren(
              Td(text = "feet"),
              Td(text = "centimetres (cm)"),
              Td(text = "30.48", isNumeric = true)
            ),
            Tr().withChildren(
              Td(text = "yards"),
              Td(text = "metres (m)"),
              Td(text = "0.91444", isNumeric = true)
            ),
            Tr().withChildren(
              Td(text = "td0001"),
              Td(text = "td0002"),
              Td(text = "td0003", isNumeric = true)
            )
          ),
          Tfoot().withChildren(
            headAndFoot
          )
        )
      ),
      VStack().withChildren(
        Code(text = """
          |code-0001
          |""".stripMargin),
        Code(colorScheme = Some("red")).withChildren(
          Text(text = "val a=1"),
          NewLine(),
          Text(text = "println(a)")
        )
      ),
      UnorderedList().withChildren(
        ListItem(text = "unordered-list-list-item1"),
        ListItem(text = "unordered-list-list-item2")
      ),
      OrderedList().withChildren(
        ListItem(text = "Ordered-list-list-item1"),
        ListItem(text = "Ordered-list-list-item2")
      )
    )
