package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiComponent, UiElement}

case class QuickTabs(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    tabs: Seq[String | Seq[UiElement]] = Nil,
    tabPanels: Seq[Seq[UiElement]] = Nil
) extends UiComponent
    with HasStyle:
  type This = QuickTabs

  def withTabs(tabs: String | Seq[UiElement]*): QuickTabs  = copy(tabs = tabs)
  def withTabPanels(tabPanels: Seq[UiElement]*): QuickTabs = copy(tabPanels = tabPanels)

  override lazy val rendered = Seq(
    Tabs(key = key + "-tabs", style = style).withChildren(
      TabList(
        key = key + "-tab-list",
        children = tabs.zipWithIndex.map:
          case (name: String, idx)             => Tab(key = s"$key-tab-$idx", text = name)
          case (elements: Seq[UiElement], idx) => Tab(key = s"$key-tab-$idx", children = elements)
      ),
      TabPanels(
        key = key + "-panels",
        children = tabPanels.zipWithIndex.map: (elements, idx) =>
          TabPanel(key = s"$key-panel-$idx", children = elements)
      )
    )
  )

  override def withStyle(v: Map[String, Any]): QuickTabs = copy(style = v)
