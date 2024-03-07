package org.terminal21.client.components.chakra

import org.terminal21.client.components.*
import org.terminal21.client.components.UiElement.{HasChildren, HasStyle}
import org.terminal21.collections.TypedMap

sealed trait CEJson extends UiElement

/** The chakra-react based components, for a complete (though bit rough) example please see
  * https://github.com/kostaskougios/terminal21-restapi/blob/main/examples/src/main/scala/tests/ChakraComponents.scala and it's related scala files under
  * https://github.com/kostaskougios/terminal21-restapi/tree/main/examples/src/main/scala/tests/chakra
  */
sealed trait ChakraElement extends CEJson with HasStyle

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
    spacing: Option[String] = None,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with OnClickEventHandler.CanHandleOnClickEvent:
  type This = Button
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
  override def withDataStore(ds: TypedMap): Button    = copy(dataStore = ds)

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
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = ButtonGroup
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
  override def withDataStore(ds: TypedMap)                 = copy(dataStore = ds)

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
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Box
  override def withChildren(cn: UiElement*): Box   = copy(children = cn)
  override def withStyle(v: Map[String, Any]): Box = copy(style = v)
  def withKey(v: String): Box                      = copy(key = v)
  def withText(v: String): Box                     = copy(text = v)
  def withBg(v: String): Box                       = copy(bg = v)
  def withW(v: String): Box                        = copy(w = v)
  def withP(v: Int): Box                           = copy(p = v)
  def withColor(v: String): Box                    = copy(color = v)
  def withAs(v: Option[String]): Box               = copy(as = v)
  override def withDataStore(ds: TypedMap)         = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/stack
  */
case class HStack(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    align: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = HStack
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withSpacing(v: String)                  = copy(spacing = Some(v))
  def withAlign(v: Option[String])            = copy(align = v)
  def withAlign(v: String)                    = copy(align = Some(v))
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class VStack(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    align: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = VStack
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withSpacing(v: String)                  = copy(spacing = Some(v))
  def withAlign(v: Option[String])            = copy(align = v)
  def withAlign(v: String)                    = copy(align = Some(v))
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class SimpleGrid(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    spacingX: Option[String] = None,
    spacingY: Option[String] = None,
    columns: Int = 2,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = SimpleGrid
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withSpacingX(v: Option[String])         = copy(spacingX = v)
  def withSpacingY(v: Option[String])         = copy(spacingY = v)
  def withColumns(v: Int)                     = copy(columns = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/editable
  */
case class Editable(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren
    with OnChangeEventHandler.CanHandleOnChangeEvent:
  type This = Editable
  override def withChildren(cn: UiElement*)          = copy(children = cn)
  override def withStyle(v: Map[String, Any])        = copy(style = v)
  def withKey(v: String)                             = copy(key = v)
  def withDefaultValue(v: String)                    = copy(defaultValue = v)
  override def withDataStore(ds: TypedMap): Editable = copy(dataStore = ds)

case class EditablePreview(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends ChakraElement:
  type This = EditablePreview
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class EditableInput(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends ChakraElement:
  type This = EditableInput
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class EditableTextarea(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends ChakraElement:
  type This = EditableTextarea
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormControl(
    key: String = Keys.nextKey,
    as: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = FormControl
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withAs(v: String)                       = copy(as = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormLabel(
    key: String = Keys.nextKey,
    text: String,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = FormLabel
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/form-control
  */
case class FormHelperText(
    key: String = Keys.nextKey,
    text: String,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = FormHelperText
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/input
  */
case class Input(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    size: String = "md",
    variant: Option[String] = None,
    defaultValue: String = "",
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with OnChangeEventHandler.CanHandleOnChangeEvent:
  type This = Input
  override def withStyle(v: Map[String, Any]): Input = copy(style = v)
  def withKey(v: String): Input                      = copy(key = v)
  def withType(v: String): Input                     = copy(`type` = v)
  def withPlaceholder(v: String): Input              = copy(placeholder = v)
  def withSize(v: String): Input                     = copy(size = v)
  def withVariant(v: Option[String]): Input          = copy(variant = v)
  def withDefaultValue(v: String): Input             = copy(defaultValue = v)
  override def withDataStore(ds: TypedMap): Input    = copy(dataStore = ds)

case class InputGroup(
    key: String = Keys.nextKey,
    size: String = "md",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = InputGroup
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSize(v: String)                     = copy(size = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class InputLeftAddon(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = InputLeftAddon
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class InputRightAddon(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = InputRightAddon
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/checkbox
  */
case class Checkbox(
    key: String = Keys.nextKey,
    text: String = "",
    defaultChecked: Boolean = false,
    isDisabled: Boolean = false,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent:
  type This = Checkbox
  override def withStyle(v: Map[String, Any])        = copy(style = v)
  def withKey(v: String)                             = copy(key = v)
  def withText(v: String)                            = copy(text = v)
  def withDefaultChecked(v: Boolean)                 = copy(defaultChecked = v)
  def withIsDisabled(v: Boolean)                     = copy(isDisabled = v)
  override def withDataStore(ds: TypedMap): Checkbox = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/radio
  */
case class Radio(
    key: String = Keys.nextKey,
    value: String,
    text: String = "",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = Radio
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withValue(v: String)                    = copy(value = v)
  def withText(v: String)                     = copy(text = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class RadioGroup(
    key: String = Keys.nextKey,
    defaultValue: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren
    with OnChangeEventHandler.CanHandleOnChangeEvent:
  type This = RadioGroup
  override def withChildren(cn: UiElement*)            = copy(children = cn)
  override def withStyle(v: Map[String, Any])          = copy(style = v)
  def withKey(v: String)                               = copy(key = v)
  def withDefaultValue(v: String)                      = copy(defaultValue = v)
  override def withDataStore(ds: TypedMap): RadioGroup = copy(dataStore = ds)

case class Center(
    key: String = Keys.nextKey,
    text: String = "",
    children: Seq[UiElement] = Nil,
    bg: Option[String] = None,
    w: Option[String] = None,
    h: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Center
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: Option[String])               = copy(bg = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Circle(
    key: String = Keys.nextKey,
    text: String = "",
    children: Seq[UiElement] = Nil,
    bg: Option[String] = None,
    w: Option[String] = None,
    h: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Circle
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: Option[String])               = copy(bg = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Square(
    key: String = Keys.nextKey,
    text: String = "",
    children: Seq[UiElement] = Nil,
    bg: Option[String] = None,
    w: Option[String] = None,
    h: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Square
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withBg(v: Option[String])               = copy(bg = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AddIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = AddIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowBackIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowBackIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowDownIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowForwardIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowForwardIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowLeftIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowLeftIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowRightIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowRightIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowUpIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ArrowUpDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ArrowUpDownIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AtSignIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = AtSignIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class AttachmentIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = AttachmentIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class BellIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = BellIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CalendarIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = CalendarIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChatIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ChatIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = CheckIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CheckCircleIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = CheckCircleIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ChevronDownIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronLeftIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ChevronLeftIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronRightIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ChevronRightIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ChevronUpIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ChevronUpIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CloseIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = CloseIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class CopyIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = CopyIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DeleteIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = DeleteIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DownloadIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = DownloadIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class DragHandleIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = DragHandleIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EditIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = EditIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class EmailIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = EmailIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ExternalLinkIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    mx: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ExternalLinkIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  def withMx(v: Option[String])               = copy(mx = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class HamburgerIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = HamburgerIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = InfoIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class InfoOutlineIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = InfoOutlineIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LinkIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = LinkIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class LockIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = LockIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MinusIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = MinusIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class MoonIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = MoonIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class NotAllowedIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = NotAllowedIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PhoneIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = PhoneIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class PlusSquareIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = PlusSquareIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = QuestionIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class QuestionOutlineIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = QuestionOutlineIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = RepeatIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class RepeatClockIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = RepeatClockIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SearchIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = SearchIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class Search2Icon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = Search2Icon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SettingsIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = SettingsIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallAddIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = SmallAddIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SmallCloseIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = SmallCloseIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SpinnerIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = SpinnerIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class StarIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = StarIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class SunIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = SunIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TimeIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = TimeIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = TriangleDownIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class TriangleUpIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = TriangleUpIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UnlockIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = UnlockIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class UpDownIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = UpDownIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ViewIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class ViewOffIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = ViewOffIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = WarningIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** generated by generateIconsCode() , https://chakra-ui.com/docs/components/icon
  */
case class WarningTwoIcon(
    key: String = Keys.nextKey,
    w: Option[String] = None,
    h: Option[String] = None,
    boxSize: Option[String] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = WarningTwoIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withW(v: Option[String])                = copy(w = v)
  def withH(v: Option[String])                = copy(h = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withColor(v: Option[String])            = copy(color = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/textarea
  */
case class Textarea(
    key: String = Keys.nextKey,
    `type`: String = "text",
    placeholder: String = "",
    size: String = "md",
    variant: Option[String] = None,
    defaultValue: String = "",
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with OnChangeEventHandler.CanHandleOnChangeEvent:
  type This = Textarea
  override def withStyle(v: Map[String, Any])        = copy(style = v)
  def withKey(v: String)                             = copy(key = v)
  def withType(v: String)                            = copy(`type` = v)
  def withPlaceholder(v: String)                     = copy(placeholder = v)
  def withSize(v: String)                            = copy(size = v)
  def withVariant(v: Option[String])                 = copy(variant = v)
  def withDefaultValue(v: String)                    = copy(defaultValue = v)
  override def withDataStore(ds: TypedMap): Textarea = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/switch
  */
case class Switch(
    key: String = Keys.nextKey,
    text: String = "",
    defaultChecked: Boolean = false,
    isDisabled: Boolean = false,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with OnChangeBooleanEventHandler.CanHandleOnChangeEvent:
  type This = Switch
  override def withStyle(v: Map[String, Any])      = copy(style = v)
  def withKey(v: String)                           = copy(key = v)
  def withText(v: String)                          = copy(text = v)
  def withDefaultChecked(v: Boolean)               = copy(defaultChecked = v)
  def withIsDisabled(v: Boolean)                   = copy(isDisabled = v)
  override def withDataStore(ds: TypedMap): Switch = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/select
  */
case class Select(
    key: String = Keys.nextKey,
    placeholder: String = "",
    defaultValue: String = "",
    bg: Option[String] = None,
    color: Option[String] = None,
    borderColor: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren
    with OnChangeEventHandler.CanHandleOnChangeEvent:
  type This = Select
  override def withStyle(v: Map[String, Any])      = copy(style = v)
  override def withChildren(cn: UiElement*)        = copy(children = cn)
  def withKey(v: String)                           = copy(key = v)
  def withPlaceholder(v: String)                   = copy(placeholder = v)
  def withDefaultValue(v: String)                  = copy(defaultValue = v)
  def withBg(v: Option[String])                    = copy(bg = v)
  def withColor(v: Option[String])                 = copy(color = v)
  def withBorderColor(v: Option[String])           = copy(borderColor = v)
  override def withDataStore(ds: TypedMap): Select = copy(dataStore = ds)

case class Option_(
    key: String = Keys.nextKey,
    value: String,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = Option_
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withValue(v: String)                    = copy(value = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/table/usage
  */
case class TableContainer(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement
    with HasChildren:
  type This = TableContainer
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
  override def withDataStore(ds: TypedMap)  = copy(dataStore = ds)

case class Table(
    key: String = Keys.nextKey,
    variant: String = "simple",
    size: String = "md",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Table
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withVariant(v: String)                  = copy(variant = v)
  def withSize(v: String)                     = copy(size = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class TableCaption(key: String = Keys.nextKey, text: String = "", style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement:
  type This = TableCaption
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Thead(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement
    with HasChildren:
  type This = Thead
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Tbody(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement
    with HasChildren:
  type This = Tbody
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Tfoot(key: String = Keys.nextKey, children: Seq[UiElement] = Nil, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement
    with HasChildren:
  type This = Tfoot
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Tr(
    key: String = Keys.nextKey,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Tr
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Th(
    key: String = Keys.nextKey,
    text: String = "",
    isNumeric: Boolean = false,
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Th
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withIsNumeric(v: Boolean)               = copy(isNumeric = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Td(
    key: String = Keys.nextKey,
    text: String = "",
    isNumeric: Boolean = false,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Td
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withIsNumeric(v: Boolean)               = copy(isNumeric = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/menu/usage
  */
case class Menu(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, children: Seq[UiElement] = Nil, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement
    with HasChildren:
  type This = Menu
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class MenuButton(
    key: String = Keys.nextKey,
    text: String = "",
    size: Option[String] = None,
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = MenuButton
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withSize(v: Option[String])             = copy(size = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class MenuList(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, children: Seq[UiElement] = Nil, dataStore: TypedMap = TypedMap.Empty)
    extends ChakraElement
    with HasChildren:
  type This = MenuList
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class MenuItem(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    text: String = "",
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren
    with OnClickEventHandler.CanHandleOnClickEvent:
  type This = MenuItem
  override def withChildren(cn: UiElement*)          = copy(children = cn)
  override def withStyle(v: Map[String, Any])        = copy(style = v)
  def withKey(v: String)                             = copy(key = v)
  def withText(v: String)                            = copy(text = v)
  override def withDataStore(ds: TypedMap): MenuItem = copy(dataStore = ds)

case class MenuDivider(key: String = Keys.nextKey, style: Map[String, Any] = Map.empty, dataStore: TypedMap = TypedMap.Empty) extends ChakraElement:
  type This = MenuDivider
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Badge(
    key: String = Keys.nextKey,
    text: String = "",
    colorScheme: Option[String] = None,
    variant: Option[String] = None,
    size: String = "md",
    children: Seq[UiElement] = Nil,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Badge
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  def withVariant(v: Option[String])          = copy(variant = v)
  def withSize(v: String)                     = copy(size = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

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
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = Image
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSrc(v: String)                      = copy(src = v)
  def withAlt(v: String)                      = copy(alt = v)
  def withBoxSize(v: Option[String])          = copy(boxSize = v)
  def withBorderRadius(v: Option[String])     = copy(borderRadius = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

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
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = Text
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
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Code(
    key: String = Keys.nextKey,
    text: String = "",
    colorScheme: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Code
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class UnorderedList(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = UnorderedList
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class OrderedList(
    key: String = Keys.nextKey,
    spacing: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = OrderedList
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class ListItem(
    key: String = Keys.nextKey,
    text: String = "",
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = ListItem
  def withText(v: String)                     = copy(text = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Alert(
    key: String = Keys.nextKey,
    status: String = "error", // error, success, warning, info
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Alert
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withStatus(v: String)                   = copy(status = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class AlertIcon(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = AlertIcon
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class AlertTitle(
    key: String = Keys.nextKey,
    text: String = "Alert!",
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = AlertTitle
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class AlertDescription(
    key: String = Keys.nextKey,
    text: String = "Something happened!",
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = AlertDescription
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withText(v: String)                     = copy(text = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/progress
  */
case class Progress(
    key: String = Keys.nextKey,
    value: Int = 50,
    colorScheme: Option[String] = None,
    size: Option[String] = None,
    hasStripe: Option[Boolean] = None,
    isIndeterminate: Option[Boolean] = None,
    style: Map[String, Any] = Map.empty,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement:
  type This = Progress
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withKey(v: String)                      = copy(key = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  def withSize(v: Option[String])             = copy(size = v)
  def withValue(v: Int)                       = copy(value = v)
  def withHasStripe(v: Option[Boolean])       = copy(hasStripe = v)
  def withIsIndeterminate(v: Option[Boolean]) = copy(isIndeterminate = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

case class Tooltip(
    key: String = Keys.nextKey,
    label: String = "tooltip.label",
    bg: Option[String] = None,
    color: Option[String] = None,
    hasArrow: Option[Boolean] = None,
    fontSize: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Seq(Text("use tooltip.withContent() to set this")),
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Tooltip
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
  override def withDataStore(ds: TypedMap)           = copy(dataStore = ds)

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
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Tabs
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withVariant(v: Option[String])          = copy(variant = v)
  def withColorScheme(v: Option[String])      = copy(colorScheme = v)
  def withSize(v: Option[String])             = copy(size = v)
  def withAlign(v: Option[String])            = copy(align = v)
  def withIsFitted(v: Option[Boolean])        = copy(isFitted = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class TabList(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = TabList
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

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
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Tab
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
  override def withDataStore(ds: TypedMap)      = copy(dataStore = ds)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class TabPanels(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = TabPanels
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** see https://chakra-ui.com/docs/components/tabs
  */
case class TabPanel(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = TabPanel
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

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
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = Breadcrumb
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withSeparator(v: String)                = copy(separator = v)
  def withSpacing(v: Option[String])          = copy(spacing = v)
  def withFontWeight(v: Option[String])       = copy(fontWeight = v)
  def withFontSize(v: Option[String])         = copy(fontSize = v)
  def withPt(v: Option[Int])                  = copy(pt = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/breadcrumb
  */
case class BreadcrumbItem(
    key: String = Keys.nextKey,
    isCurrentPage: Option[Boolean] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren:
  type This = BreadcrumbItem
  def withKey(v: String)                      = copy(key = v)
  override def withChildren(cn: UiElement*)   = copy(children = cn)
  override def withStyle(v: Map[String, Any]) = copy(style = v)
  def withIsCurrentPage(v: Option[Boolean])   = copy(isCurrentPage = v)
  override def withDataStore(ds: TypedMap)    = copy(dataStore = ds)

/** https://chakra-ui.com/docs/components/breadcrumb
  */
case class BreadcrumbLink(
    key: String = Keys.nextKey,
    text: String = "breadcrumblink.text",
    href: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren
    with OnClickEventHandler.CanHandleOnClickEvent:
  type This = BreadcrumbLink
  def withKey(v: String)                                   = copy(key = v)
  override def withChildren(cn: UiElement*)                = copy(children = cn)
  override def withStyle(v: Map[String, Any])              = copy(style = v)
  def withHref(v: Option[String])                          = copy(href = v)
  def withText(v: String)                                  = copy(text = v)
  override def withDataStore(ds: TypedMap): BreadcrumbLink = copy(dataStore = ds)

case class Link(
    key: String = Keys.nextKey,
    text: String = "link.text",
    href: String = "#",
    isExternal: Option[Boolean] = None,
    color: Option[String] = None,
    style: Map[String, Any] = Map.empty,
    children: Seq[UiElement] = Nil,
    dataStore: TypedMap = TypedMap.Empty
) extends ChakraElement
    with HasChildren
    with OnClickEventHandler.CanHandleOnClickEvent:
  type This = Link
  def withKey(v: String)                         = copy(key = v)
  override def withChildren(cn: UiElement*)      = copy(children = cn)
  override def withStyle(v: Map[String, Any])    = copy(style = v)
  def withIsExternal(v: Option[Boolean])         = copy(isExternal = v)
  def withIsExternal(v: Boolean)                 = copy(isExternal = Some(v))
  def withHref(v: String)                        = copy(href = v)
  def withText(v: String)                        = copy(text = v)
  def withColor(v: String)                       = copy(color = Some(v))
  def withColor(v: Option[String])               = copy(color = v)
  override def withDataStore(ds: TypedMap): Link = copy(dataStore = ds)
