package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiComponent, UiElement}

case class QuickTable(
    key: String = Keys.nextKey,
    variant: String = "striped",
    colorScheme: String = "teal",
    size: String = "mg"
) extends UiComponent
    with HasStyle:
  val head = Thead()
  val body = Tbody()

  val table                              = Table(variant = variant, colorScheme = Some(colorScheme), size = size)
    .withChildren(
      head,
      body
    )
  @volatile var tableContainer           = TableContainer().withChildren(table)
  @volatile var children: Seq[UiElement] = Seq(tableContainer)

  def headers(headers: String*): QuickTable            = headersElements(headers.map(h => Text(text = h)): _*)
  def headersElements(headers: UiElement*): QuickTable =
    head.children = headers.map(h => Th(children = Seq(h)))
    this

  def rows(data: Seq[Seq[String]]): QuickTable = rowsElements(data.map(_.map(c => Text(text = c))))

  def rowsElements(data: Seq[Seq[UiElement]]): QuickTable =
    body.children = data.map: row =>
      Tr(children = row.map(c => Td().withChildren(c)))
    this

  def style: Map[String, String]            = tableContainer.style
  def style_=(s: Map[String, String]): Unit = tableContainer.style = s

  def caption(text: String): QuickTable =
    table.addChildren(TableCaption(text = text))
    this
