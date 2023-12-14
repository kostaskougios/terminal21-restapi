package org.terminal21.client.components.chakra

import org.terminal21.client.ui.UiElement
import org.terminal21.client.{ConnectedSession, OnClickEventHandler}
import org.terminal21.common.Keys

sealed trait ChakraElement extends UiElement

case class Button(key: String = Keys.nextKey, text: String) extends ChakraElement:
  def onClick(h: OnClickEventHandler)(using session: ConnectedSession): Button =
    session.addEventHandler(key, h)
    this

case class Box(key: String = Keys.nextKey, var text: String = "", var props: ChakraProps, var children: Seq[UiElement] = Nil) extends ChakraElement:
  def withChildren(cn: UiElement*): Box =
    children = cn
    this

case class SimpleGrid(
    key: String = Keys.nextKey,
    var spacing: String = "",
    var spacingX: String = "",
    var spacingY: String = "",
    var columns: Int = 2,
    var children: Seq[UiElement] = Nil
) extends ChakraElement:
  def withChildren(cn: UiElement*) =
    children = cn
    this

  def addChildren(e: UiElement*) =
    children = children ++ e
    this
