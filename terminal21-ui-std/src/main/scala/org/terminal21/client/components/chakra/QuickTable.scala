package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement

object QuickTable:
  case class QuickTableBuilder(
      headers: Seq[UiElement],
      data: Seq[Seq[UiElement]],
      caption: Option[String] = None,
      variant: String = "striped",
      colorScheme: String = "teal",
      size: String = "mg"
  ):
    def withHeaders(hs: Seq[UiElement]): QuickTableBuilder = copy(headers = hs)
    def withStringHeaders(hs: String*): QuickTableBuilder  = copy(headers = hs.map(h => Text(text = h)))

    def withRowData(dt: Seq[Seq[UiElement]]): QuickTableBuilder    = copy(data = dt)
    def withRowStringData(dt: Seq[Seq[String]]): QuickTableBuilder = copy(data = dt.map(row => row.map(c => Text(text = c))))

    def build: TableContainer =
      val hs = headers.map: h =>
        Th().withChildren(h)
      TableContainer().withChildren(
        Table(variant = variant, colorScheme = Some(colorScheme), size = size)
          .withChildren(
            Thead().withChildren(
              Tr().withChildren(hs: _*)
            ),
            Tbody(
              children = data.map: row =>
                Tr(
                  children = row.map: column =>
                    Td().withChildren(column)
                )
            )
          )
          .addChildren(caption.map(c => TableCaption(text = c)).toList: _*)
      )
  def quickTable(
      caption: Option[String] = None,
      variant: String = "striped",
      colorScheme: String = "teal",
      size: String = "mg"
  ) = QuickTableBuilder(Nil, Nil, caption, variant, colorScheme, size)
