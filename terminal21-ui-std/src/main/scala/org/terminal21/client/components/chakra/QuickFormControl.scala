package org.terminal21.client.components.chakra

import org.terminal21.client.components.{Keys, UiComponent, UiElement}
import org.terminal21.client.components.UiElement.HasStyle

case class QuickFormControl(
    key: String = Keys.nextKey,
    style: Map[String, Any] = Map.empty,
    label: Option[String] = None,
    inputGroup: Seq[UiElement] = Nil,
    helperText: Option[String] = None
) extends UiComponent
    with HasStyle[QuickFormControl]:
  lazy val rendered: Seq[UiElement] =
    val ch: Seq[UiElement] =
      label.map(l => FormLabel(key = key + "-label", text = l)).toSeq ++
        Seq(InputGroup(key = key + "-ig").withChildren(inputGroup: _*)) ++
        helperText.map(h => FormHelperText(key = key + "-helper", text = h))
    Seq(
      FormControl(key = key + "-fc", style = style).withChildren(ch: _*)
    )

  def withLabel(label: String): QuickFormControl       = copy(label = Some(label))
  def withInputGroup(ig: UiElement*): QuickFormControl = copy(inputGroup = ig)
  def withHelperText(text: String): QuickFormControl   = copy(helperText = Some(text))

  override def withStyle(v: Map[String, Any]): QuickFormControl = copy(style = v)
