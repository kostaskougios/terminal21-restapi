package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.{Current, HasChildren, HasEventHandler, HasStyle}
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.client.{ConnectedSession, OnChangeBooleanEventHandler, OnChangeEventHandler, OnClickEventHandler}

sealed trait CEJson extends UiElement

/** The chakra-react based components, for a complete (though bit rough) example please see
  * https://github.com/kostaskougios/terminal21-restapi/blob/main/examples/src/main/scala/tests/ChakraComponents.scala and it's related scala files under
  * https://github.com/kostaskougios/terminal21-restapi/tree/main/examples/src/main/scala/tests/chakra
  */
sealed trait ChakraElement[A <: ChakraElement[A]] extends CEJson with HasStyle[A] with Current[A]

/** https://chakra-ui.com/docs/components/button
  */
case class Button(
    key: String = Keys.nextKey,
    text: String = "Ok",
    size: Option[String] = None,
    variant: Option[String] = None,
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    leftIcon: Option[UiElement] = None,
    rightIcon: Option[UiElement] = None,
    isActive: Option[Boolean] = None,
    isDisabled: Option[Boolean] = None,
    isLoading: Option[Boolean] = None,
    isAttached: Option[Boolean] = None,
    spacing: Option[String] = None
) extends ChakraElement[Button]
    with OnClickEventHandler.CanHandleOnClickEvent[Button]:
  override def style(v: Map[String, Any]): Button = copy(style = v)

/** https://chakra-ui.com/docs/components/button
  */
case class ButtonGroup(
    key: String = Keys.nextKey,
    variant: Option[String] = None,
    spacing: Option[String] = None,
    size: Option[String] = None,
    width: Option[String] = None,
    height: Option[String] = None,
    border: Option[String] = None,
    borderColor: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[ButtonGroup]
    with HasChildren[ButtonGroup]:
  override def withChildren(cn: UiElement*)            = copy(children = cn)
  override def style(v: Map[String, Any]): ButtonGroup = copy(style = v)

/** https://chakra-ui.com/docs/components/box
  */
case class Box(
    key: String = Keys.nextKey,
    text: String = "",
    bg: String = "",
    w: String = "",
    p: Int = 0,
    color: String = "",
    style: Map[String, Any] = Map.empty,
    as: Option[String] = None,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Box]
    with HasChildren[Box]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/stack
  */
case class HStack(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    align: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[HStack]
    with HasChildren[HStack]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class VStack(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    align: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[VStack]
    with HasChildren[VStack]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class SimpleGrid(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    spacingX: Option[String] = None,
    spacingY: Option[String] = None,
    columns: Int = 2,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SimpleGrid]
    with HasChildren[SimpleGrid]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/editable
  */
case class Editable(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    value: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Editable]
    with HasEventHandler
    with HasChildren[Editable]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Editable]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler =
    newValue => session.modified(copy(value = newValue))
  override def withChildren(cn: UiElement*)                                         = copy(children = cn)
  override def style(v: Map[String, Any])                                           = copy(style = v)

case class EditablePreview(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[EditablePreview]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class EditableInput(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[EditableInput]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class EditableTextarea(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[EditableTextarea]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormControl(
    key: String = Keys.nextKey,
    as: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[FormControl]
    with HasChildren[FormControl]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormLabel(
    key: String = Keys.nextKey,
    text: String,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[FormLabel]
    with HasChildren[FormLabel]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormHelperText(
    key: String = Keys.nextKey,
    text: String,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[FormHelperText]
    with HasChildren[FormHelperText]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/input
  */
case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    size: String = "md",
    variant: Option[String] = None,
    value: String = "",
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Input]
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Input]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(value = newValue))
  override def style(v: Map[String, Any])                                           = copy(style = v)

case class InputGroup(
    key: String = Keys.nextKey,
    size: String = "md",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[InputGroup]
    with HasChildren[InputGroup]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class InputLeftAddon(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[InputLeftAddon]
    with HasChildren[InputLeftAddon]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class InputRightAddon(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[InputRightAddon]
    with HasChildren[InputRightAddon]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/checkbox
  */
case class Checkbox(
    key: String = Keys.nextKey,
    text: String = "",
    defaultChecked: Boolean = false,
    isDisabled: Boolean = false,
    style: Map[String, Any] = Map.empty,
    checkedV: Option[Boolean] = None
) extends ChakraElement[Checkbox]
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Checkbox]:
  def checked: Boolean                                                              = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(checkedV = Some(newValue.toBoolean)))
  override def style(v: Map[String, Any])                                           = copy(style = v)

/** https://chakra-ui.com/docs/components/radio
  */
case class Radio(
    key: String = Keys.nextKey,
    value: String,
    text: String = "",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Radio]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class RadioGroup(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    value: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[RadioGroup]
    with HasEventHandler
    with HasChildren[RadioGroup]
    with OnChangeEventHandler.CanHandleOnChangeEvent[RadioGroup]:

  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(value = newValue))
  override def withChildren(cn: UiElement*)                                         = copy(children = cn)
  override def style(v: Map[String, Any])                                           = copy(style = v)

case class Center(
    key: String = Keys.nextKey,
    text: String = "",
    children: Seq[UiElement] = Nil,
    bg: Option[String] = None,
    w: Option[String] = None,
    h: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Center]
    with HasChildren[Center]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Circle(
    key: String = Keys.nextKey,
    text: String = "",
    children: Seq[UiElement] = Nil,
    bg: Option[String] = None,
    w: Option[String] = None,
    h: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Circle]
    with HasChildren[Circle]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Square(
    key: String = Keys.nextKey,
    text: String = "",
    children: Seq[UiElement] = Nil,
    bg: Option[String] = None,
    w: Option[String] = None,
    h: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Square]
    with HasChildren[Square]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AddIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[AddIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowBackIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowBackIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowDownIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowForwardIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowForwardIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowLeftIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowLeftIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowRightIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowRightIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowUpIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ArrowUpDownIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AtSignIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[AtSignIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AttachmentIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[AttachmentIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class BellIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[BellIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CalendarIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[CalendarIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChatIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ChatIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[CheckIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckCircleIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[CheckCircleIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ChevronDownIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronLeftIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ChevronLeftIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronRightIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ChevronRightIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronUpIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ChevronUpIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CloseIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[CloseIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CopyIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[CopyIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DeleteIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[DeleteIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DownloadIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[DownloadIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DragHandleIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[DragHandleIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EditIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[EditIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EmailIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[EmailIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ExternalLinkIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ExternalLinkIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class HamburgerIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[HamburgerIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[InfoIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoOutlineIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[InfoOutlineIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LinkIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[LinkIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LockIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[LockIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MinusIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[MinusIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MoonIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[MoonIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class NotAllowedIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[NotAllowedIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PhoneIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[PhoneIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PlusSquareIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[PlusSquareIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[QuestionIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionOutlineIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[QuestionOutlineIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[RepeatIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatClockIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[RepeatClockIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SearchIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SearchIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class Search2Icon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Search2Icon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SettingsIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SettingsIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallAddIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SmallAddIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallCloseIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SmallCloseIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SpinnerIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SpinnerIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class StarIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[StarIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SunIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[SunIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TimeIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[TimeIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[TriangleDownIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleUpIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[TriangleUpIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UnlockIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[UnlockIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UpDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[UpDownIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ViewIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewOffIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ViewOffIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[WarningIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningTwoIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[WarningTwoIcon]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** https://chakra-ui.com/docs/components/textarea
  */
case class Textarea(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    size: String = "md",
    variant: Option[String] = None,
    value: String = "",
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Textarea]
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Textarea]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(value = newValue))
  override def style(v: Map[String, Any])                                           = copy(style = v)

/** https://chakra-ui.com/docs/components/switch
  */
case class Switch(
    key: String = Keys.nextKey,
    text: String = "",
    defaultChecked: Boolean = false,
    isDisabled: Boolean = false,
    style: Map[String, Any] = Map.empty,
    checkedV: Option[Boolean] = None
) extends ChakraElement[Switch]
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Switch]:
  def checked: Boolean                                                              = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(checkedV = Some(newValue.toBoolean)))
  override def style(v: Map[String, Any])                                           = copy(style = v)

/** https://chakra-ui.com/docs/components/select
  */
case class Select(
    key: String = Keys.nextKey,
    placeholder: String = "",
    value: String = "",
    bg: Option[String] = None,
    color: Option[String] = None,
    borderColor: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Select]
    with HasEventHandler
    with HasChildren[Select]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Select]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(value = newValue))
  override def style(v: Map[String, Any])                                           = copy(style = v)
  override def withChildren(cn: UiElement*)                                         = copy(children = cn)

case class Option_(
    key: String = Keys.nextKey,
    value: String,
    text: String = "",
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Option_]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** https://chakra-ui.com/docs/components/table/usage
  */
case class TableContainer(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[TableContainer]
    with HasChildren[TableContainer]:
  override def style(v: Map[String, Any])                       = copy(style = v)
  def withRowStringData(data: Seq[Seq[String]]): TableContainer = withRowData(data.map(_.map(c => Text(text = c))))
  def withRowData(data: Seq[Seq[UiElement]]): TableContainer    =
    val tableBodies = children
      .collect:
        case t: Table =>
          t.children.collect:
            case b: Tbody => b
      .flatten
    val newTrs      = data.map: row =>
      Tr(
        children = row.map: column =>
          Td().withChildren(column)
      )
    for b <- tableBodies do b.withChildren(newTrs: _*)
    this

  override def withChildren(cn: UiElement*) = copy(children = cn)

case class Table(
    key: String = Keys.nextKey,
    variant: String = "simple",
    size: String = "md",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Table]
    with HasChildren[Table]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class TableCaption(key: String = Keys.nextKey, text: String = "", style: Map[String, Any] = Map.empty) extends ChakraElement[TableCaption]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class Thead(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[Thead]
    with HasChildren[Thead]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Tbody(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[Tbody]
    with HasChildren[Tbody]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Tfoot(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[Tfoot]
    with HasChildren[Tfoot]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Tr(
    key: String = Keys.nextKey,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Tr]
    with HasChildren[Tr]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Th(
    key: String = Keys.nextKey,
    text: String = "",
    isNumeric: Boolean = false,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Th]
    with HasChildren[Th]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class Td(
    key: String = Keys.nextKey,
    text: String = "",
    isNumeric: Boolean = false,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Td]
    with HasChildren[Td]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/menu/usage
  */
case class Menu(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, children: Seq[UiElement] = Nil)
    extends ChakraElement[Menu]
    with HasChildren[Menu]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class MenuButton(
    key: String = Keys.nextKey,
    text: String = "",
    size: Option[String] = None,
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[MenuButton]
    with HasChildren[MenuButton]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class MenuList(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, children: Seq[UiElement] = Nil)
    extends ChakraElement[MenuList]
    with HasChildren[MenuList]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class MenuItem(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    text: String = "",
    children: Seq[UiElement] = Nil
) extends ChakraElement[MenuItem]
    with HasChildren[MenuItem]
    with OnClickEventHandler.CanHandleOnClickEvent[MenuItem]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

case class MenuDivider(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[MenuDivider]:
  override def style(v: Map[String, Any]) = copy(style = v)

case class Badge(
    key: String = Keys.nextKey,
    text: String = "",
    colorScheme: Option[String] = None,
    variant: Option[String] = None,
    size: String = "md",
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Badge]
    with HasChildren[Badge]:
  override def withChildren(cn: UiElement*) = copy(children = cn)
  override def style(v: Map[String, Any])   = copy(style = v)

/** https://chakra-ui.com/docs/components/image/usage
  *
  * Note: you can also add images under ~/.terminal21/web/images (where the server runs) and use a relative url to access them, i.e.
  *
  * Image(src = "/web/images/logo1.png")
  */
case class Image(
    key: String = Keys.nextKey,
    src: String = "",
    alt: String = "",
    boxSize: Option[String] = None,
    borderRadius: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Image]:
  override def style(v: Map[String, Any]) = copy(style = v)

/** https://chakra-ui.com/docs/components/text
  */
case class Text(
    key: String = Keys.nextKey,
    text: String = "text.text is empty. Did you accidentally assigned the text to the `key` field?",
    fontSize: Option[String] = None,
    noOfLines: Option[Int] = None,
    color: Option[String] = None,
    as: Option[String] = None,
    align: Option[String] = None,
    casing: Option[String] = None,
    decoration: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Text]:
  override def style(v: Map[String, Any]) = copy(style = v)
