package tests.chakra

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.*
import tests.chakra.Common.*

object Editables:
  def components(events: Events): Seq[UiElement] =
    val editable1 = Editable(key = "editable1", defaultValue = "Please type here")
      .withChildren(
        EditablePreview(),
        EditableInput()
      )

    val editable2 = Editable(key = "editable2", defaultValue = "For longer maybe-editable texts\nUse an EditableTextarea\nIt uses a textarea control.")
      .withChildren(
        EditablePreview(),
        EditableTextarea()
      )

    val statusMsg = (events.changedValue(editable1).map(newValue => s"editable1 newValue = $newValue") ++ events
      .changedValue(editable2)
      .map(newValue => s"editable2 newValue = $newValue")).headOption.getOrElse("This will reflect any changes in the form.")

    val status = Box(text = statusMsg)

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
