package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Editables:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val status = Box(text = "This will reflect any changes in the form.")

    val editable1I = Editable(defaultValue = "Please type here").withChildren(
      EditablePreview(),
      EditableInput()
    )

    val editable1 = editable1I.onChange: newValue =>
      status.withText(s"editable1 newValue = $newValue, verify editable1.value = ${editable1I.current.value}").renderChanges()

    val editable2I = Editable(defaultValue = "For longer maybe-editable texts\nUse an EditableTextarea\nIt uses a textarea control.").withChildren(
      EditablePreview(),
      EditableTextarea()
    )
    val editable2  = editable2I.onChange: newValue =>
      status.withText(s"editable2 newValue = $newValue, verify editable2.value = ${editable2I.current.value}").renderChanges()

    Seq(
      commonBox(text = "Editables"),
      SimpleGrid(columns = 2).withChildren(
        Box(text = "Editable"),
        editable1,
        Box(text = "Editable with text area"),
        editable2
      ),
      status
    )
