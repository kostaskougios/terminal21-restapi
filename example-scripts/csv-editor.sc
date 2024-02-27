#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A quick and dirty csv file editor for small csv files.
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.model.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import org.apache.commons.io.FileUtils
import java.io.File

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the csv file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents =
  if file.exists() then FileUtils.readFileToString(file, "UTF-8")
  else "type,damage points,hit points\nmage,10dp,20hp\nwarrior,20dp,30hp"

val csv = contents.split("\n").map(_.split(",").toSeq).toSeq

Sessions
  .withNewSession(s"csv-editor-$fileName", s"CsvEdit: $fileName")
  .connect: session =>
    given ConnectedSession = session
    println(s"Now open ${session.uiUrl} to view the UI")
    val editor = new CsvEditor(csv)
    editor.run()

class CsvEditor(csv: Seq[Seq[String]])(using session: ConnectedSession):
  case class CsvModel(save: Boolean, exitWithoutSave: Boolean, csv: Seq[Seq[String]])
  private given Model[CsvModel] = Model(CsvModel(false, false, Nil))
  val saveAndExit = Button(text = "Save & Exit").onClick: event =>
    import event.*
    val csv = tableCells.map: row =>
      row.map: editable =>
        editable.current.value
    handled.withModel(CsvModel(true, false, csv)).terminate
  val exit = Button(text = "Exit Without Saving").onClick: event =>
    import event.*
    handled.withModel(model.copy(exitWithoutSave = true)).terminate
  val status = Box()

  val tableCells =
    csv.map: row =>
      row.map: column =>
        newEditable(column)

  def run(): Unit =
    for handled <- controller.render().handledEventsIterator.lastOption.filter(_.model.save)
    do save(handled.model.csv)

  def components: Seq[UiElement] =
    Seq(
      QuickTable(variant = "striped", colorScheme = "teal", size = "mg")
        .withCaption("Please edit the csv contents above and click save to save and exit")
        .withRows(tableCells),
      HStack().withChildren(
        saveAndExit,
        exit,
        status
      )
    )

  /** @return
    *   true if the user clicked "Save", false if the user clicked "Exit" or closed the session
    */
  def controller: Controller[CsvModel] =
    Controller(components)

  def save(data: Seq[Seq[String]]): Unit =
    FileUtils.writeStringToFile(file, data.map(_.mkString(",")).mkString("\n"), "UTF-8")
    println(s"Csv file saved to $file")

  private def newEditable(value: String): Editable =
    Editable(defaultValue = value)
      .withChildren(
        EditablePreview(),
        EditableInput()
      )
      .onChange: event =>
        event.handled.withRenderChanges(status.withText(s"Changed a cell value to ${event.newValue}"))
