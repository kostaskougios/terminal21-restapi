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
  override def withStyle(v: Map[String, Any]): Button = copy(style = v)
  def withKey(v: String)                              = copy(key = v)
  def withText(v: String)                             = copy(text = v)
  def withSize(v: Option[String])                     = copy(size = v)
  def withVariant(v: Option[String])                  = copy(variant = v)
  def withColorScheme(v: Option[String])              = copy(colorScheme = v)
  def withLeftIcon(v: Option[UiElement])              = copy(leftIcon = v)
  def withLeftIcon(v: UiElement)                      = copy(leftIcon = Some(v))
  def withRightIcon(v: Option[UiElement])             = copy(rightIcon = v)
  def withRightIcon(v: UiElement)                     = copy(rightIcon = Some(v))
  def withIsActive(v: Option[Boolean])                = copy(isActive = v)
  def withIsDisabled(v: Option[Boolean])              = copy(isDisabled = v)
  def withIsLoading(v: Option[Boolean])               = copy(isLoading = v)
  def withIsAttached(v: Option[Boolean])              = copy(isAttached = v)
  def withSpacing(v: Option[String])                  = copy(spacing = v)

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
  override def withChildren(cn: UiElement*)                = copy(children = cn)
  override def withStyle(v: Map[String, Any]): ButtonGroup = copy(style = v)
  def withKey(v: String)                                   = copy(key = v)
  def withVariant(v: Option[String])                       = copy(variant = v)
  def withSpacing(v: Option[String])                       = copy(spacing = v)
  def withSize(v: Option[String])                          = copy(size = v)
  def withWidth(v: Option[String])                         = copy(width = v)
  def withHeight(v: Option[String])                        = copy(height = v)
  def withBorder(v: Option[String])                        = copy(border = v)
  def withBorderColor(v: Option[String])                   = copy(borderColor = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: String)                       = copy(bg = v)
  def withW(v: String)                        = copy(w = v)
  def withP(v: Int)                           = copy(p = v)
  def withColor(v: String)                    = copy(color = v)
  def withAs(v: Option[String])               = copy(as = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withAlign(v: Option[String])            = copy(align = v)

case class VStack(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    align: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[VStack]
    with HasChildren[VStack]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withAlign(v: Option[String])            = copy(align = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withSpacingX(v: Option[String])         = copy(spacingX = v)
  def withSpacingY(v: Option[String])         = copy(spacingY = v)
  def withColumns(v: Int)                     = copy(columns = v)

/** https://chakra-ui.com/docs/components/editable
  */
case class Editable(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    valueReceived: Option[String] = None, // use value instead
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Editable]
    with HasEventHandler
    with HasChildren[Editable]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Editable]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler =
    newValue => session.modified(copy(valueReceived = Some(newValue)))
  override def withChildren(cn: UiElement*)                                         = copy(children = cn)
  override def withStyle(v: Map[String, Any])                                       = copy(style = v)
  def withKey(v: String)                                                            = copy(key = v)
  def withDefaultValue(v: String)                                                   = copy(defaultValue = v)
  def value                                                                         = valueReceived.getOrElse(defaultValue)

case class EditablePreview(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[EditablePreview]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class EditableInput(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[EditableInput]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class EditableTextarea(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[EditableTextarea]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormControl(
    key: String = Keys.nextKey,
    as: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[FormControl]
    with HasChildren[FormControl]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withAs(v: String)                       = copy(as = v)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormLabel(
    key: String = Keys.nextKey,
    text: String,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[FormLabel]
    with HasChildren[FormLabel]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormHelperText(
    key: String = Keys.nextKey,
    text: String,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[FormHelperText]
    with HasChildren[FormHelperText]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

/** https://chakra-ui.com/docs/components/input
  */
case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    size: String = "md",
    variant: Option[String] = None,
    defaultValue: String = "",
    valueReceived: Option[String] = None, // use value instead
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Input]
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Input]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(valueReceived = Some(newValue)))
  override def withStyle(v: Map[String, Any]): Input                                = copy(style = v)
  def withKey(v: String): Input                                                     = copy(key = v)
  def withType(v: String): Input                                                    = copy(`type` = v)
  def withPlaceholder(v: String): Input                                             = copy(placeholder = v)
  def withSize(v: String): Input                                                    = copy(size = v)
  def withVariant(v: Option[String]): Input                                         = copy(variant = v)
  def withDefaultValue(v: String): Input                                            = copy(defaultValue = v)
  def value: String                                                                 = valueReceived.getOrElse(defaultValue)

case class InputGroup(
    key: String = Keys.nextKey,
    size: String = "md",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[InputGroup]
    with HasChildren[InputGroup]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSize(v: String)                     = copy(size = v)

case class InputLeftAddon(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[InputLeftAddon]
    with HasChildren[InputLeftAddon]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class InputRightAddon(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[InputRightAddon]
    with HasChildren[InputRightAddon]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

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
  override def withStyle(v: Map[String, Any])                                       = copy(style = v)
  def withKey(v: String)                                                            = copy(key = v)
  def withText(v: String)                                                           = copy(text = v)
  def withDefaultChecked(v: Boolean)                                                = copy(defaultChecked = v)
  def withIsDisabled(v: Boolean)                                                    = copy(isDisabled = v)

/** https://chakra-ui.com/docs/components/radio
  */
case class Radio(
    key: String = Keys.nextKey,
    value: String,
    text: String = "",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Radio]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withValue(v: String)                    = copy(value = v)
  def withText(v: String)                     = copy(text = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)

case class RadioGroup(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    valueV: Option[String] = None, // use value
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[RadioGroup]
    with HasEventHandler
    with HasChildren[RadioGroup]
    with OnChangeEventHandler.CanHandleOnChangeEvent[RadioGroup]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(valueV = Some(newValue)))
  override def withChildren(cn: UiElement*)                                         = copy(children = cn)
  override def withStyle(v: Map[String, Any])                                       = copy(style = v)
  def value: String                                                                 = valueV.getOrElse(defaultValue)
  def withKey(v: String)                                                            = copy(key = v)
  def withDefaultValue(v: String)                                                   = copy(defaultValue = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: Option[String])               = copy(bg = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: Option[String])               = copy(bg = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: Option[String])               = copy(bg = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ExternalLinkIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    mx: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[ExternalLinkIcon]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  def withMx(v: Option[String])               = copy(mx = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)

/** https://chakra-ui.com/docs/components/textarea
  */
case class Textarea(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    size: String = "md",
    variant: Option[String] = None,
    defaultValue: String = "",
    valueReceived: Option[String] = None, // use value instead
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Textarea]
    with HasEventHandler
    with OnChangeEventHandler.CanHandleOnChangeEvent[Textarea]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(valueReceived = Some(newValue)))
  override def withStyle(v: Map[String, Any])                                       = copy(style = v)
  def withKey(v: String)                                                            = copy(key = v)
  def withType(v: String)                                                           = copy(`type` = v)
  def withPlaceholder(v: String)                                                    = copy(placeholder = v)
  def withSize(v: String)                                                           = copy(size = v)
  def withVariant(v: Option[String])                                                = copy(variant = v)
  def withDefaultValue(v: String)                                                   = copy(defaultValue = v)
  def value                                                                         = valueReceived.getOrElse(defaultValue)

/** https://chakra-ui.com/docs/components/switch
  */
case class Switch(
    key: String = Keys.nextKey,
    text: String = "",
    defaultChecked: Boolean = false,
    isDisabled: Boolean = false,
    style: Map[String, Any] = Map.empty,
    checkedV: Option[Boolean] = None // use checked
) extends ChakraElement[Switch]
    with HasEventHandler
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent[Switch]:
  def checked: Boolean                                                              = checkedV.getOrElse(defaultChecked)
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(checkedV = Some(newValue.toBoolean)))
  override def withStyle(v: Map[String, Any])                                       = copy(style = v)
  def withKey(v: String)                                                            = copy(key = v)
  def withText(v: String)                                                           = copy(text = v)
  def withDefaultChecked(v: Boolean)                                                = copy(defaultChecked = v)
  def withIsDisabled(v: Boolean)                                                    = copy(isDisabled = v)

/** https://chakra-ui.com/docs/components/select
  */
case class Select(
    key: String = Keys.nextKey,
    placeholder: String = "",
    defaultValue: String = "",
    valueReceived: Option[String] = None, // use value instead
    bg: Option[String] = None,
    color: Option[String] = None,
    borderColor: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Select]
    with HasEventHandler
    with HasChildren[Select]
    with OnChangeEventHandler.CanHandleOnChangeEvent[Select]:
  override def defaultEventHandler(session: ConnectedSession): OnChangeEventHandler = newValue => session.modified(copy(valueReceived = Some(newValue)))
  override def withStyle(v: Map[String, Any])                                       = copy(style = v)
  override def withChildren(cn: UiElement*)                                         = copy(children = cn)
  def withKey(v: String)                                                            = copy(key = v)
  def withPlaceholder(v: String)                                                    = copy(placeholder = v)
  def withDefaultValue(v: String)                                                   = copy(defaultValue = v)
  def withBg(v: Option[String])                                                     = copy(bg = v)
  def withColor(v: Option[String])                                                  = copy(color = v)
  def withBorderColor(v: Option[String])                                            = copy(borderColor = v)
  def value                                                                         = valueReceived.getOrElse(defaultValue)

case class Option_(
    key: String = Keys.nextKey,
    value: String,
    text: String = "",
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Option_]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withValue(v: String)                    = copy(value = v)
  def withText(v: String)                     = copy(text = v)

/** https://chakra-ui.com/docs/components/table/usage
  */
case class TableContainer(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[TableContainer]
    with HasChildren[TableContainer]:
  override def withStyle(v: Map[String, Any])                   = copy(style = v)
  def withKey(v: String)                                        = copy(key = v)
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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withVariant(v: String)                  = copy(variant = v)
  def withSize(v: String)                     = copy(size = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)

case class TableCaption(key: String = Keys.nextKey, text: String = "", style: Map[String, Any] = Map.empty) extends ChakraElement[TableCaption]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class Thead(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[Thead]
    with HasChildren[Thead]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class Tbody(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[Tbody]
    with HasChildren[Tbody]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class Tfoot(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty)
    extends ChakraElement[Tfoot]
    with HasChildren[Tfoot]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class Tr(
    key: String = Keys.nextKey,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Tr]
    with HasChildren[Tr]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class Th(
    key: String = Keys.nextKey,
    text: String = "",
    isNumeric: Boolean = false,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Th]
    with HasChildren[Th]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withIsNumeric(v: Boolean)               = copy(isNumeric = v)

case class Td(
    key: String = Keys.nextKey,
    text: String = "",
    isNumeric: Boolean = false,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Td]
    with HasChildren[Td]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withIsNumeric(v: Boolean)               = copy(isNumeric = v)

/** https://chakra-ui.com/docs/components/menu/usage
  */
case class Menu(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, children: Seq[UiElement] = Nil)
    extends ChakraElement[Menu]
    with HasChildren[Menu]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class MenuButton(
    key: String = Keys.nextKey,
    text: String = "",
    size: Option[String] = None,
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[MenuButton]
    with HasChildren[MenuButton]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withSize(v: Option[String])             = copy(size = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)

case class MenuList(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, children: Seq[UiElement] = Nil)
    extends ChakraElement[MenuList]
    with HasChildren[MenuList]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class MenuItem(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    text: String = "",
    children: Seq[UiElement] = Nil
) extends ChakraElement[MenuItem]
    with HasChildren[MenuItem]
    with OnClickEventHandler.CanHandleOnClickEvent[MenuItem]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class MenuDivider(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty) extends ChakraElement[MenuDivider]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

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
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  def withVariant(v: Option[String])          = copy(variant = v)
  def withSize(v: String)                     = copy(size = v)

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
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSrc(v: String)                      = copy(src = v)
  def withAlt(v: String)                      = copy(alt = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withBorderRadius(v: Option[String])     = copy(borderRadius = v)

/** https://chakra-ui.com/docs/components/text
  */
case class Text(
    key: String = Keys.nextKey,
    text: String = "text.text is empty. Did you accidentally assigned the text to the `key` field? Do Text(text=...)",
    fontSize: Option[String] = None,
    noOfLines: Option[Int] = None,
    color: Option[String] = None,
    as: Option[String] = None,
    align: Option[String] = None,
    casing: Option[String] = None,
    decoration: Option[String] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Text]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withFontSize(v: Option[String])         = copy(fontSize = v)
  def withNoOfLines(v: Option[Int])           = copy(noOfLines = v)
  def withColor(v: Option[String])            = copy(color = v)
  def withAs(v: Option[String])               = copy(as = v)
  def withAlign(v: Option[String])            = copy(align = v)
  def withCasing(v: Option[String])           = copy(casing = v)
  def withDecoration(v: Option[String])       = copy(decoration = v)

case class Code(
    key: String = Keys.nextKey,
    text: String = "",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Code]
    with HasChildren[Code]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)

case class UnorderedList(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[UnorderedList]
    with HasChildren[UnorderedList]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)

case class OrderedList(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[OrderedList]
    with HasChildren[OrderedList]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)

case class ListItem(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[ListItem]
    with HasChildren[ListItem]:
  def withText(v: String)                     = copy(text = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class Alert(
    key: String = Keys.nextKey,
    status: String = "error", // error, success, warning, info
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Alert]
    with HasChildren[Alert]:
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withStatus(v: String)                   = copy(status = v)

case class AlertIcon(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[AlertIcon]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)

case class AlertTitle(
    key: String = Keys.nextKey,
    text: String = "Alert!",
    style: Map[String, Any] = Map.empty
) extends ChakraElement[AlertTitle]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

case class AlertDescription(
    key: String = Keys.nextKey,
    text: String = "Something happened!",
    style: Map[String, Any] = Map.empty
) extends ChakraElement[AlertDescription]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)

/** https://chakra-ui.com/docs/components/progress
  */
case class Progress(
    key: String = Keys.nextKey,
    value: Int = 50,
    colorScheme: Option[String] = None,
    size: Option[String] = None,
    hasStripe: Option[Boolean] = None,
    isIndeterminate: Option[Boolean] = None,
    style: Map[String, Any] = Map.empty
) extends ChakraElement[Progress]:
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  def withSize(v: Option[String])             = copy(size = v)
  def withValue(v: Int)                       = copy(value = v)
  def withHasStripe(v: Option[Boolean])       = copy(hasStripe = v)
  def withIsIndeterminate(v: Option[Boolean]) = copy(isIndeterminate = v)

case class Tooltip(
    key: String = Keys.nextKey,
    label: String = "tooltip.label",
    bg: Option[String] = None,
    color: Option[String] = None,
    hasArrow: Option[Boolean] = None,
    fontSize: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Seq(Text("use tooltip.withContent() to set this"))
) extends ChakraElement[Tooltip]
    with HasChildren[Tooltip]:
  override def withStyle(v: Map[String, Any])        = copy(style = v)
  def withContent(cn: UiElement)                     = withChildren(cn)
  def withKey(v: String)                             = copy(key = v)
  def withBg(v: Option[String])                      = copy(bg = v)
  def withColor(v: Option[String])                   = copy(color = v)
  def withHasArrow(v: Option[Boolean])               = copy(hasArrow = v)
  def withFontSize(v: Option[String])                = copy(fontSize = v)
  override def noChildren                            = copy(children = Nil)
  override def withChildren(cn: UiElement*): Tooltip =
    if cn.size != 1 then throw new IllegalArgumentException("tooltip takes 1 only child") else copy(children = cn)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class Tabs(
    key: String = Keys.nextKey,
    defaultIndex: Int = 0,
    variant: Option[String] = Some("enclosed"), // line,enclosed, enclosed-colored, soft-rounded, solid-rounded, and unstyled.
    align: Option[String] = None,               // start, center, end
    colorScheme: Option[String] = None,
    size: Option[String] = None,
    isFitted: Option[Boolean] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Tabs]
    with HasChildren[Tabs]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withVariant(v: Option[String])          = copy(variant = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  def withSize(v: Option[String])             = copy(size = v)
  def withAlign(v: Option[String])            = copy(align = v)
  def withIsFitted(v: Option[Boolean])        = copy(isFitted = v)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class TabList(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[TabList]
    with HasChildren[TabList]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class Tab(
    key: String = Keys.nextKey,
    text: String = "tab.text",
    isDisabled: Option[Boolean] = None,
    _selected: Option[Map[String, Any]] = None,
    _hover: Option[Map[String, Any]] = None,
    _active: Option[Map[String, Any]] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Tab]
    with HasChildren[Tab]:
  def withKey(v: String)                        = copy(key = v)
  def withText(v: String)                       = copy(text = v)
  override def withChildren(cn: UiElement*)     = copy(children = cn)
  override def withStyle(v: Map[String, Any])   = copy(style = v)
  def withIsDisabled(v: Option[Boolean])        = copy(isDisabled = v)
  def withSelected(v: Map[String, Any])         = copy(_selected = Some(v))
  def withSelected(v: Option[Map[String, Any]]) = copy(_selected = v)
  def withHover(v: Map[String, Any])            = copy(_hover = Some(v))
  def withHover(v: Option[Map[String, Any]])    = copy(_hover = v)
  def withActive(v: Map[String, Any])           = copy(_active = Some(v))
  def withActive(v: Option[Map[String, Any]])   = copy(_active = v)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class TabPanels(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[TabPanels]
    with HasChildren[TabPanels]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class TabPanel(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[TabPanel]
    with HasChildren[TabPanel]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)

/** https://chakra-ui.com/docs/components/breadcrumb
  */
case class Breadcrumb(
    key: String = Keys.nextKey,
    separator: String = "/",
    spacing: Option[String] = None,
    fontWeight: Option[String] = None,
    fontSize: Option[String] = None,
    pt: Option[Int] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Breadcrumb]
    with HasChildren[Breadcrumb]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withSeparator(v: String)                = copy(separator = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withFontWeight(v: Option[String])       = copy(fontWeight = v)
  def withFontSize(v: Option[String])         = copy(fontSize = v)
  def withPt(v: Option[Int])                  = copy(pt = v)

/** https://chakra-ui.com/docs/components/breadcrumb
  */
case class BreadcrumbItem(
    key: String = Keys.nextKey,
    isCurrentPage: Option[Boolean] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[BreadcrumbItem]
    with HasChildren[BreadcrumbItem]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withIsCurrentPage(v: Option[Boolean])   = copy(isCurrentPage = v)

/** https://chakra-ui.com/docs/components/breadcrumb
  */
case class BreadcrumbLink(
    key: String = Keys.nextKey,
    text: String = "breadcrumblink.text",
    href: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[BreadcrumbLink]
    with HasChildren[BreadcrumbLink]
    with OnClickEventHandler.CanHandleOnClickEvent[BreadcrumbLink]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withHref(v: Option[String])             = copy(href = v)
  def withText(v: String)                     = copy(text = v)

case class Link(
    key: String = Keys.nextKey,
    text: String = "link.text",
    href: String = "#",
    isExternal: Option[Boolean] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil
) extends ChakraElement[Link]
    with HasChildren[Link]
    with OnClickEventHandler.CanHandleOnClickEvent[Link]:
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withIsExternal(v: Option[Boolean])      = copy(isExternal = v)
  def withIsExternal(v: Boolean)              = copy(isExternal = Some(v))
  def withHref(v: String)                     = copy(href = v)
  def withText(v: String)                     = copy(text = v)
  def withColor(v: String)                    = copy(color = Some(v))
  def withColor(v: Option[String])            = copy(color = v)
