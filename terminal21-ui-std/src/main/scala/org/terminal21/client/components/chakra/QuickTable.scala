package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiComponent, UiElement}
import org.terminal21.collections.TypedMap

case class QuickTable(
    key: String = Keys.nextKey,
    variant: String = "striped",
    colorScheme: String = "teal",
    size: String = "mg",
    style: Map[String, Any] = Map.empty,
    caption: Option[String] = None,
    headers: Seq[Any] = Nil,
    rows: Seq[Seq[Any]] = Nil,
    dataStore: TypedMap = TypedMap.empty
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
    val head           = Thead(
      key = subKey("thead"),
      children = Seq(
        Tr(
          key = subKey("thead-tr"),
          children = headers.zipWithIndex.map: (h, i) =>
            Th(
              key = subKey(s"thead-tr-th-$i"),
              children = Seq(
                h match
                  case u: UiElement => u
                  case c            => Text(text = c.toString)
              )
            )
        )
      )
    )
    val body           = Tbody(
      key = subKey("tb"),
      children = rows.zipWithIndex.map: (row, i) =>
        Tr(
          key = subKey(s"tb-tr-$i"),
          children = row.zipWithIndex.map: (c, i) =>
            Td(
              key = subKey(s"tb-th-$i"),
              children = Seq(
                c match
                  case u: UiElement => u
                  case c            => Text(text = c.toString)
              )
            )
        )
    )
    val table          = Table(
      key = subKey("t"),
      variant = variant,
      colorScheme = Some(colorScheme),
      size = size,
      children = caption.map(text => TableCaption(text = text)).toSeq ++ Seq(head, body)
    )
    val tableContainer = TableContainer(key = subKey("tc"), style = style, children = Seq(table))
    Seq(tableContainer)

  def withHeaders(headers: String*): QuickTable            = copy(headers = headers.map(h => Text(text = h)))
  def withHeadersElements(headers: UiElement*): QuickTable = copy(headers = headers)

  /** @param data
    *   A mix of plain types or UiElement. If it is a UiElement, it will be rendered otherwise if it is anything else the `.toString` method will be used to
    *   render it.
    * @return
    *   QuickTable
    */
  def withRows(data: Seq[Seq[Any]]): QuickTable               = copy(rows = data)
  def withRowsElements(data: Seq[Seq[UiElement]]): QuickTable = copy(rows = data)

  def caption(text: String): QuickTable                   = copy(caption = Some(text))
  override def withStyle(v: Map[String, Any]): QuickTable = copy(style = v)
  override def withDataStore(ds: TypedMap)                = copy(dataStore = ds)
