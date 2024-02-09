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
  def withKey(v: String)               = copy(key = v)
  def withVariant(v: String)           = copy(variant = v)
  def withColorScheme(v: String)       = copy(colorScheme = v)
  def withSize(v: String)              = copy(size = v)
  def withCaption(v: Option[String])   = copy(caption = v)
  def withCaption(v: String)           = copy(caption = Some(v))
  def withHeaders(v: Seq[UiElement])   = copy(headers = v)
  def withRows(v: Seq[Seq[UiElement]]) = copy(rows = v)

  override lazy val rendered: Seq[UiElement] =
    val head           = Thead(key = key + "-th", children = Seq(Tr(children = headers.map(h => Th(children = Seq(h))))))
    val body           = Tbody(
      key = key + "-tb",
      children = rows.map: row =>
        Tr(children = row.map(c => Td(children = Seq(c))))
    )
    val table          = Table(
      key = key + "-t",
      variant = variant,
      colorScheme = Some(colorScheme),
      size = size,
      children = caption.map(text => TableCaption(text = text)).toSeq ++ Seq(head, body)
    )
    val tableContainer = TableContainer(key = key + "-tc", style = style, children = Seq(table))
    Seq(tableContainer)

  def headers(headers: String*): QuickTable               = copy(headers = headers.map(h => Text(text = h)))
  def headersElements(headers: UiElement*): QuickTable    = copy(headers = headers)
  def rows(data: Seq[Seq[Any]]): QuickTable               = copy(rows = data.map(_.map:
    case u: UiElement => u
    case c            => Text(text = c.toString)
  ))
  def rowsElements(data: Seq[Seq[UiElement]]): QuickTable = copy(rows = data)

  def caption(text: String): QuickTable                   = copy(caption = Some(text))
  override def withStyle(v: Map[String, Any]): QuickTable = copy(style = v)
