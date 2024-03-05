package tests.chakra

import org.terminal21.client.Model
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Editables:
  def components(m: ChakraModel): Seq[UiElement] =
    val editable1 = Editable(key = "editable1", defaultValue = "Please type here")
      .withChildren(
        EditablePreview(),
        EditableInput()
      )
      .onChange: event =>
        import event.*
        handled.mapModel(_.copy(editableStatus = s"editable1 newValue = $newValue"))

    val editable2 = Editable(key = "editable2", defaultValue = "For longer maybe-editable texts\nUse an EditableTextarea\nIt uses a textarea control.")
      .withChildren(
        EditablePreview(),
        EditableTextarea()
      )
      .onChange: event =>
        import event.*
        handled.mapModel(_.copy(editableStatus = s"editable2 newValue = $newValue"))

    val status = Box(text = m.editableStatus)

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
