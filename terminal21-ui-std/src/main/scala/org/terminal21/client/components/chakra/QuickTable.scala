package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement

object QuickTable:
  def quickTable(
      headers: Seq[UiElement],
      data: Seq[Seq[UiElement]],
      caption: Option[String] = None,
      variant: String = "striped",
      colorScheme: String = "teal",
      size: String = "mg"
  ) =
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
