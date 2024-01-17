package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiComponent, UiElement}

case class QuickTable(
    key: String = Keys.nextKey,
    variant: String = "striped",
    colorScheme: String = "teal",
    size: String = "mg",
    style: Map[String, Any] = Map.empty,
    caption: Option[String] = None,
    headers: Seq[UiElement] = Nil,
    rows: Seq[Seq[UiElement]] = Nil
) extends UiComponent
    with HasStyle[QuickTable]:

  override def rendered: Seq[UiElement] =
    val head           = Thead(children = Seq(Tr(children = headers.map(h => Th(children = Seq(h))))))
    val body           = Tbody(
      children = rows.map: row =>
        Tr(children = row.map(c => Td().withChildren(c)))
    )
    val table          = Table(
      variant = variant,
      colorScheme = Some(colorScheme),
      size = size,
      children = caption.map(text => TableCaption(text = text)).toSeq ++ Seq(head, body)
    )
    val tableContainer = TableContainer().withChildren(table)
    Seq(tableContainer)

  def headers(headers: String*): QuickTable               = copy(headers = headers.map(h => Text(text = h)))
  def headersElements(headers: UiElement*): QuickTable    = copy(headers = headers)
  def rows(data: Seq[Seq[Any]]): QuickTable               = copy(rows = data.map(_.map(c => Text(text = c.toString))))
  def rowsElements(data: Seq[Seq[UiElement]]): QuickTable = copy(rows = data)

  def caption(text: String): QuickTable               = copy(caption = Some(text))
  override def style(v: Map[String, Any]): QuickTable = copy(style = v)
