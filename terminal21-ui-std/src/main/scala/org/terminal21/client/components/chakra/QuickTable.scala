package org.terminal21.client.components.chakra

import org.terminal21.client.components.Keys.linearKeys
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
    with HasStyle:
  type This = QuickTable
  def withKey(v: String)             = copy(key = v)
  def withVariant(v: String)         = copy(variant = v)
  def withColorScheme(v: String)     = copy(colorScheme = v)
  def withSize(v: String)            = copy(size = v)
  def withCaption(v: Option[String]) = copy(caption = v)
  def withCaption(v: String)         = copy(caption = Some(v))

  override lazy val rendered: Seq[UiElement] =
    val head           = Thead(key = subKey("-th"), children = Seq(Tr(children = headers.map(h => Th(children = Seq(h))))))
    val body           = Tbody(
      key = subKey("-tb"),
      children = rows.map: row =>
        Tr(children = row.map(c => Td(children = Seq(c))))
    )
    val table          = Table(
      key = subKey("-t"),
      variant = variant,
      colorScheme = Some(colorScheme),
      size = size,
      children = caption.map(text => TableCaption(text = text)).toSeq ++ Seq(head, body)
    )
    val tableContainer = TableContainer(key = subKey("-tc"), style = style, children = Seq(table))
    linearKeys(key, tableContainer)

  def withHeaders(headers: String*): QuickTable            = copy(headers = headers.map(h => Text(text = h)))
  def withHeadersElements(headers: UiElement*): QuickTable = copy(headers = headers)

  /** @param data
    *   A mix of plain types or UiElement. If it is a UiElement, it will be rendered otherwise if it is anything else the `.toString` method will be used to
    *   render it.
    * @return
    *   QuickTable
    */
  def withRows(data: Seq[Seq[Any]]): QuickTable               = copy(rows = data.map(_.map:
    case u: UiElement => u
    case c            => Text(text = c.toString)
  ))
  def withRowsElements(data: Seq[Seq[UiElement]]): QuickTable = copy(rows = data)

  def caption(text: String): QuickTable                   = copy(caption = Some(text))
  override def withStyle(v: Map[String, Any]): QuickTable = copy(style = v)
