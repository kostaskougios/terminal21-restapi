package org.terminal21.client.components.chakra

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiComponent, UiElement}
import org.terminal21.collections.TypedMap

case class QuickFormControl(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    label: Option[String] = None,
    inputGroup: Seq[UiElement] = Nil,
    helperText: Option[String] = None,
    dataStore: TypedMap = TypedMap.empty
) extends UiComponent
    with HasStyle:
  type This = QuickFormControl
  lazy val rendered: Seq[UiElement] =
    val ch: Seq[UiElement] =
      label.map(l => FormLabel(key = subKey("label"), text = l)).toSeq ++
        Seq(InputGroup(key = subKey("ig")).withChildren(inputGroup*)) ++
        helperText.map(h => FormHelperText(key = subKey("helper"), text = h))
    Seq(FormControl(key = subKey("fc"), style = style).withChildren(ch: _*))

  def withLabel(label: String): QuickFormControl       = copy(label = Some(label))
  def withInputGroup(ig: UiElement*): QuickFormControl = copy(inputGroup = ig)
  def withHelperText(text: String): QuickFormControl   = copy(helperText = Some(text))

  override def withStyle(v: Map[String, Any]): QuickFormControl = copy(style = v)
  override def withKey(key: String): QuickFormControl           = copy(key = key)
  override def withDataStore(ds: TypedMap)                      = copy(dataStore = ds)
