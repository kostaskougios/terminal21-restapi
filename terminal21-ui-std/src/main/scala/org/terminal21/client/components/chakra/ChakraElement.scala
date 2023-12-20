package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler}
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.client.{ConnectedSession, OnChangeBooleanEventHandler, OnChangeEventHandler, OnClickEventHandler}

sealed trait ChakraElement extends UiElement

/** https://chakra-ui.com/docs/components/button
  */
case class Button(
    key: String = Keys.nextKey,
    @volatile var text: String = "Ok",
    @volatile var size: Option[String] = None,
    @volatile var variant: Option[String] = None,
    @volatile var colorScheme: Option[String] = None
) extends ChakraElement:
  def onClick(h: OnClickEventHandler)(using session: ConnectedSession): Button =
    session.addEventHandler(key, h)
    this

/** https://chakra-ui.com/docs/components/button
  */
case class ButtonGroup(
    key: String = Keys.nextKey,
    @volatile var variant: Option[String] = None,
    @volatile var spacing: Option[String] = None,
    @volatile var size: Option[String] = None,
    @volatile var width: Option[String] = None,
    @volatile var height: Option[String] = None,
    @volatile var border: Option[String] = None,
    @volatile var borderColor: Option[String] = None,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[ButtonGroup]

/** https://chakra-ui.com/docs/components/box
  */
case class Box(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var props: ChakraProps = ChakraProps(),
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[Box]

/** https://chakra-ui.com/docs/components/stack
  */
case class HStack(key: String = Keys.nextKey, @volatile var spacing: Option[String] = None, @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[HStack]
case class VStack(key: String = Keys.nextKey, @volatile var spacing: Option[String] = None, @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[VStack]

case class SimpleGrid(
    key: String = Keys.nextKey,
    @volatile var spacing: Option[String] = None,
    @volatile var spacingX: Option[String] = None,
    @volatile var spacingY: Option[String] = None,
    @volatile var columns: Int = 2,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[SimpleGrid]

/** https://chakra-ui.com/docs/components/editable
  */
case class Editable(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    @volatile var value: String = "",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasEventHandler
    with HasChildren[Editable]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Editable]:
  if value == "" then value = defaultValue
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

case class EditablePreview(key: String = Keys.nextKey)  extends ChakraElement
case class EditableInput(key: String = Keys.nextKey)    extends ChakraElement
case class EditableTextarea(key: String = Keys.nextKey) extends ChakraElement

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
    `type`: String = "text",
    placeholder: String = "",
    @volatile var size: String = "md",
    @volatile var variant: Option[String] = None,
    @volatile var value: String = "",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasEventHandler
    with HasChildren[Input]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Input]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

/** https://chakra-ui.com/docs/components/checkbox
  */
case class Checkbox(key: String = Keys.nextKey, @volatile var text: String = "", defaultChecked: Boolean = false, @volatile var isDisabled: Boolean = false)
    extends ChakraElement
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Checkbox]:
  @volatile private var checkedV: Option[Boolean]        = None
  def checked: Boolean                                   = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler: OnChangeEventHandler = newValue => checkedV = Some(newValue.toBoolean)

/** https://chakra-ui.com/docs/components/radio
  */
case class Radio(key: String = Keys.nextKey, value: String, @volatile var text: String = "", @volatile var colorScheme: Option[String] = None)
    extends ChakraElement
case class RadioGroup(key: String = Keys.nextKey, defaultValue: String = "", @volatile var value: String = "", @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasEventHandler
    with HasChildren[RadioGroup]
    with OnChangeEventHandler.CanHandleOnChangeEvent[RadioGroup]:
  if value == "" then value = defaultValue

  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue
