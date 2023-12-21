package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object DataDisplay:
  def components(using session: ConnectedSession): Seq[UiElement] = {
    val headAndFoot = Tr().withChildren(
      Th(text = "To convert"),
      Th(text = "into"),
      Th(text = "multiply by", isNumeric = true)
    )
    Seq(
      commonBox(text = "Tables"),
      TableContainer().withChildren(
        Table(variant = "striped", colorScheme = Some("teal"), size = "lg").withChildren(
          TableCaption(text = "Imperial to metric conversion factors"),
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
            )
          ),
          Tfoot().withChildren(
            headAndFoot
          )
        )
      )
    )
  }
