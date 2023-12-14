package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler}
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.client.{ConnectedSession, OnChangeEventHandler, OnClickEventHandler}

sealed trait ChakraElement extends UiElement

case class Button(key: String = Keys.nextKey, text: String) extends ChakraElement:
  def onClick(h: OnClickEventHandler)(using session: ConnectedSession): Button =
    session.addEventHandler(key, h)
    this

case class Box(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var props: ChakraProps = ChakraProps(),
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[Box]

case class SimpleGrid(
    key: String = Keys.nextKey,
    @volatile var spacing: String = "",
    @volatile var spacingX: String = "",
    @volatile var spacingY: String = "",
    @volatile var columns: Int = 2,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[SimpleGrid]

/** https://chakra-ui.com/docs/components/editable
  */
case class Editable(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    @volatile var value: String = ""
) extends ChakraElement
    with HasEventHandler:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

  def onChange(h: OnChangeEventHandler)(using session: ConnectedSession): Editable =
    session.addEventHandler(key, h)
    this

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormControl(key: String = Keys.nextKey, as: String = "", @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[FormControl]

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormLabel(key: String = Keys.nextKey, @volatile var text: String, @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[FormLabel]

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormHelperText(key: String = Keys.nextKey, @volatile var text: String, @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[FormHelperText]

/** https://chakra-ui.com/docs/components/input
  */
case class Input(
    key: String = Keys.nextKey,
    `type`: String,
    placeholder: String = "",
    size: String = "md",
    @volatile var variant: String = "Flushed",
    @volatile var value: String = "",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasEventHandler
    with HasChildren[Input]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue
