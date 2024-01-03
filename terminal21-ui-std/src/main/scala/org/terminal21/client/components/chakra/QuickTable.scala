package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement

object QuickTable:
  def quickTable(data: Seq[Seq[UiElement]], caption: Option[String] = None, variant: String = "striped", colorScheme: String = "teal", size: String = "mg") =
    TableContainer().withChildren(
      Table(variant = variant, colorScheme = Some(colorScheme), size = size)
        .withChildren(
          Thead(),
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
