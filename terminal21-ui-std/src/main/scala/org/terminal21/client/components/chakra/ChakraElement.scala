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
    @volatile var value: String = ""
) extends ChakraElement
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Input]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

case class InputGroup(
    key: String = Keys.nextKey,
    @volatile var size: String = "md",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[InputGroup]

case class InputLeftAddon(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[InputLeftAddon]

case class InputRightAddon(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[InputRightAddon]

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

case class Center(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var bg: Option[String] = None,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement
    with HasChildren[Center]

case class Circle(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var bg: Option[String] = None,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement
    with HasChildren[Circle]

case class Square(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var bg: Option[String] = None,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement
    with HasChildren[Square]

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AddIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowBackIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowForwardIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowLeftIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowRightIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AtSignIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AttachmentIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class BellIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CalendarIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChatIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckCircleIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronLeftIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronRightIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronUpIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CloseIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CopyIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DeleteIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DownloadIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DragHandleIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EditIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EmailIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ExternalLinkIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class HamburgerIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoOutlineIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LinkIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LockIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MinusIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MoonIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class NotAllowedIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PhoneIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PlusSquareIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionOutlineIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatClockIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SearchIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class Search2Icon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SettingsIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallAddIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallCloseIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SpinnerIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class StarIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SunIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TimeIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleUpIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UnlockIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UpDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewOffIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningTwoIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None
) extends ChakraElement

/** https://chakra-ui.com/docs/components/textarea
  */
case class Textarea(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    @volatile var size: String = "md",
    @volatile var variant: Option[String] = None,
    @volatile var value: String = ""
) extends ChakraElement
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Textarea]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

/** https://chakra-ui.com/docs/components/switch
  */
case class Switch(key: String = Keys.nextKey, @volatile var text: String = "", defaultChecked: Boolean = false, @volatile var isDisabled: Boolean = false)
    extends ChakraElement
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Switch]:
  @volatile private var checkedV: Option[Boolean]        = None
  def checked: Boolean                                   = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler: OnChangeEventHandler = newValue => checkedV = Some(newValue.toBoolean)
