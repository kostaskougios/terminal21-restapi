package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.{HasChildren, HasEventHandler, HasStyle}
import org.terminal21.client.components.{Keys, UiElement}
import org.terminal21.client.{OnChangeBooleanEventHandler, OnChangeEventHandler, OnClickEventHandler}

/** The chakra-react based components, for a complete (though bit rough) example please see
  * https://github.com/kostaskougios/terminal21-restapi/blob/main/examples/src/main/scala/tests/ChakraComponents.scala and it's related scala files under
  * https://github.com/kostaskougios/terminal21-restapi/tree/main/examples/src/main/scala/tests/chakra
  */
sealed trait ChakraElement extends UiElement with HasStyle

/** https://chakra-ui.com/docs/components/button
  */
case class Button(
    key: String = Keys.nextKey,
    @volatile var text: String = "Ok",
    @volatile var size: Option[String] = None,
    @volatile var variant: Option[String] = None,
    @volatile var colorScheme: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var leftIcon: Option[UiElement] = None,
    @volatile var rightIcon: Option[UiElement] = None,
    @volatile var isActive: Option[Boolean] = None,
    @volatile var isDisabled: Option[Boolean] = None,
    @volatile var isLoading: Option[Boolean] = None,
    @volatile var isAttached: Option[Boolean] = None,
    @volatile var spacing: Option[String] = None
) extends ChakraElement
    with OnClickEventHandler.CanHandleOnClickEvent[Button]

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
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[ButtonGroup]:
  override def copyNoChildren: ButtonGroup = copy(children = Nil)

/** https://chakra-ui.com/docs/components/box
  */
case class Box(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var bg: String = "",
    @volatile var w: String = "",
    @volatile var p: Int = 0,
    @volatile var color: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var as: Option[String] = None,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[Box]:
  override def copyNoChildren: Box = copy(children = Nil)

/** https://chakra-ui.com/docs/components/stack
  */
case class HStack(
    key: String = Keys.nextKey,
    @volatile var spacing: Option[String] = None,
    @volatile var align: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[HStack]:
  override def copyNoChildren: HStack = copy(children = Nil)
case class VStack(
    key: String = Keys.nextKey,
    @volatile var spacing: Option[String] = None,
    @volatile var align: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[VStack]:
  override def copyNoChildren: VStack = copy(children = Nil)

case class SimpleGrid(
    key: String = Keys.nextKey,
    @volatile var spacing: Option[String] = None,
    @volatile var spacingX: Option[String] = None,
    @volatile var spacingY: Option[String] = None,
    @volatile var columns: Int = 2,
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[SimpleGrid]:
  override def copyNoChildren: SimpleGrid = copy(children = Nil)

/** https://chakra-ui.com/docs/components/editable
  */
case class Editable(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    @volatile var value: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasEventHandler
    with HasChildren[Editable]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Editable]:
  if value == "" then value = defaultValue
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue
  override def copyNoChildren: Editable                  = copy(children = Nil)

case class EditablePreview(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty)  extends ChakraElement
case class EditableInput(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty)    extends ChakraElement
case class EditableTextarea(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty) extends ChakraElement

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormControl(
    key: String = Keys.nextKey,
    as: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[FormControl]:
  override def copyNoChildren: FormControl = copy(children = Nil)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormLabel(
    key: String = Keys.nextKey,
    @volatile var text: String,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[FormLabel]:
  override def copyNoChildren: FormLabel = copy(children = Nil)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormHelperText(
    key: String = Keys.nextKey,
    @volatile var text: String,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[FormHelperText]:
  override def copyNoChildren: FormHelperText = copy(children = Nil)

/** https://chakra-ui.com/docs/components/input
  */
case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    @volatile var size: String = "md",
    @volatile var variant: Option[String] = None,
    @volatile var value: String = "",
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Input]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

case class InputGroup(
    key: String = Keys.nextKey,
    @volatile var size: String = "md",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[InputGroup]:
  override def copyNoChildren: InputGroup = copy(children = Nil)

case class InputLeftAddon(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[InputLeftAddon]:
  override def copyNoChildren: InputLeftAddon = copy(children = Nil)

case class InputRightAddon(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[InputRightAddon]:
  override def copyNoChildren: InputRightAddon = copy(children = Nil)

/** https://chakra-ui.com/docs/components/checkbox
  */
case class Checkbox(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    defaultChecked: Boolean = false,
    @volatile var isDisabled: Boolean = false,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Checkbox]:
  @volatile private var checkedV: Option[Boolean]        = None
  def checked: Boolean                                   = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler: OnChangeEventHandler = newValue => checkedV = Some(newValue.toBoolean)

/** https://chakra-ui.com/docs/components/radio
  */
case class Radio(
    key: String = Keys.nextKey,
    value: String,
    @volatile var text: String = "",
    @volatile var colorScheme: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
case class RadioGroup(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    @volatile var value: String = "",
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasEventHandler
    with HasChildren[RadioGroup]
    with OnChangeEventHandler.CanHandleOnChangeEvent[RadioGroup]:
  if value == "" then value = defaultValue

  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

  override def copyNoChildren: RadioGroup = copy(children = Nil)

case class Center(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var bg: Option[String] = None,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[Center]:
  override def copyNoChildren: Center = copy(children = Nil)

case class Circle(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var bg: Option[String] = None,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[Circle]:
  override def copyNoChildren: Circle = copy(children = Nil)

case class Square(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var bg: Option[String] = None,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[Square]:
  override def copyNoChildren: Square = copy(children = Nil)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AddIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowBackIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowForwardIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowLeftIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowRightIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AtSignIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AttachmentIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class BellIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CalendarIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChatIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckCircleIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronLeftIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronRightIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronUpIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CloseIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CopyIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DeleteIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DownloadIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DragHandleIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EditIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EmailIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ExternalLinkIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class HamburgerIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoOutlineIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LinkIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LockIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MinusIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MoonIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class NotAllowedIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PhoneIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PlusSquareIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionOutlineIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatClockIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SearchIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class Search2Icon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SettingsIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallAddIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallCloseIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SpinnerIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class StarIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SunIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TimeIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleUpIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UnlockIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UpDownIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewOffIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningTwoIcon(
    key: String = Keys.nextKey,
    @volatile var w: Option[String] = None,
    @volatile var h: Option[String] = None,
    @volatile var boxSize: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** https://chakra-ui.com/docs/components/textarea
  */
case class Textarea(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    @volatile var size: String = "md",
    @volatile var variant: Option[String] = None,
    @volatile var value: String = "",
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Textarea]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

/** https://chakra-ui.com/docs/components/switch
  */
case class Switch(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    defaultChecked: Boolean = false,
    @volatile var isDisabled: Boolean = false,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Switch]:
  @volatile private var checkedV: Option[Boolean]        = None
  def checked: Boolean                                   = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler: OnChangeEventHandler = newValue => checkedV = Some(newValue.toBoolean)

/** https://chakra-ui.com/docs/components/select
  */
case class Select(
    key: String = Keys.nextKey,
    placeholder: String = "",
    @volatile var value: String = "",
    @volatile var bg: Option[String] = None,
    @volatile var color: Option[String] = None,
    @volatile var borderColor: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasEventHandler
    with HasChildren[Select]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Select]:
  override def defaultEventHandler: OnChangeEventHandler = newValue => value = newValue

  override def copyNoChildren: Select = copy(children = Nil)

case class Option_(
    key: String = Keys.nextKey,
    value: String,
    @volatile var text: String = "",
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** https://chakra-ui.com/docs/components/table/usage
  */
case class TableContainer(key: String = Keys.nextKey, @volatile var children: Seq[UiElement] = Nil, @volatile var style: Map[String, Any] = Map.empty)
    extends ChakraElement
    with HasChildren[TableContainer]:
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

  override def copyNoChildren: TableContainer = copy(children = Nil)

case class Table(
    key: String = Keys.nextKey,
    @volatile var variant: String = "simple",
    @volatile var size: String = "md",
    @volatile var colorScheme: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[Table]:
  override def copyNoChildren: Table = copy(children = Nil)
case class TableCaption(key: String = Keys.nextKey, @volatile var text: String = "", @volatile var style: Map[String, Any] = Map.empty) extends ChakraElement
case class Thead(key: String = Keys.nextKey, @volatile var children: Seq[UiElement] = Nil, @volatile var style: Map[String, Any] = Map.empty)
    extends ChakraElement
    with HasChildren[Thead]:
  override def copyNoChildren: Thead = copy(children = Nil)
case class Tbody(key: String = Keys.nextKey, @volatile var children: Seq[UiElement] = Nil, @volatile var style: Map[String, Any] = Map.empty)
    extends ChakraElement
    with HasChildren[Tbody]:
  override def copyNoChildren: Tbody = copy(children = Nil)
case class Tfoot(key: String = Keys.nextKey, @volatile var children: Seq[UiElement] = Nil, @volatile var style: Map[String, Any] = Map.empty)
    extends ChakraElement
    with HasChildren[Tfoot]:
  override def copyNoChildren: Tfoot = copy(children = Nil)
case class Tr(
    key: String = Keys.nextKey,
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[Tr]:
  override def copyNoChildren: Tr = copy(children = Nil)
case class Th(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    isNumeric: Boolean = false,
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[Th]:
  override def copyNoChildren: Th = copy(children = Nil)
case class Td(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    isNumeric: Boolean = false,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[Td]:
  override def copyNoChildren: Td = copy(children = Nil)

/** https://chakra-ui.com/docs/components/menu/usage
  */
case class Menu(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty, @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[Menu]:
  override def copyNoChildren: Menu = copy(children = Nil)
case class MenuButton(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var size: Option[String] = None,
    @volatile var colorScheme: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[MenuButton]:
  override def copyNoChildren: MenuButton = copy(children = Nil)
case class MenuList(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty, @volatile var children: Seq[UiElement] = Nil)
    extends ChakraElement
    with HasChildren[MenuList]:
  override def copyNoChildren: MenuList = copy(children = Nil)
case class MenuItem(
    key: String = Keys.nextKey,
    @volatile var style: Map[String, Any] = Map.empty,
    @volatile var text: String = "",
    @volatile var children: Seq[UiElement] = Nil
) extends ChakraElement
    with HasChildren[MenuItem]
    with OnClickEventHandler.CanHandleOnClickEvent[MenuItem]:
  override def copyNoChildren: MenuItem = copy(children = Nil)

case class MenuDivider(key: String = Keys.nextKey, @volatile var style: Map[String, Any] = Map.empty) extends ChakraElement

case class Badge(
    key: String = Keys.nextKey,
    @volatile var text: String = "",
    @volatile var colorScheme: Option[String] = None,
    @volatile var variant: Option[String] = None,
    @volatile var size: String = "md",
    @volatile var children: Seq[UiElement] = Nil,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
    with HasChildren[Badge]:
  override def copyNoChildren: Badge = copy(children = Nil)

/** https://chakra-ui.com/docs/components/image/usage
  *
  * Note: you can also add images under ~/.terminal21/web/images (where the server runs) and use a relative url to access them, i.e.
  *
  * Image(src = "/web/images/logo1.png")
  */
case class Image(
    key: String = Keys.nextKey,
    @volatile var src: String = "",
    @volatile var alt: String = "",
    @volatile var boxSize: Option[String] = None,
    @volatile var borderRadius: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement

/** https://chakra-ui.com/docs/components/text
  */
case class Text(
    key: String = Keys.nextKey,
    @volatile var text: String = "text.text is empty. Did you accidentally assigned the text to the `key` field?",
    @volatile var fontSize: Option[String] = None,
    @volatile var noOfLines: Option[Int] = None,
    @volatile var color: Option[String] = None,
    @volatile var as: Option[String] = None,
    @volatile var align: Option[String] = None,
    @volatile var casing: Option[String] = None,
    @volatile var decoration: Option[String] = None,
    @volatile var style: Map[String, Any] = Map.empty
) extends ChakraElement
