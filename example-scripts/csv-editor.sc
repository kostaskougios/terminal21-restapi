#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A quick and dirty csv file editor for small csv files.
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*

import org.terminal21.client.components.*
import org.terminal21.client.model.*
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
  val saveAndExit = Button(text = "Save & Exit")
  val exit = Button(text = "Exit Without Saving")
  val status = Box()

  val tableCells =
    csv.map: row =>
      row.map: column =>
        newEditable(column)

  def run(): Unit =
    components.render()
    if processEvents then
      save()
      status.withText("Csv file saved, exiting.").renderChanges()
      Thread.sleep(1000)

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
  def processEvents: Boolean =
    registerCsvEditorEventHandlers(Controller(false)).lastModelOption.getOrElse(false)

  def save(): Unit =
    val data = currentCsvValue
    FileUtils.writeStringToFile(file, data, "UTF-8")

  def currentCsvValue: String = tableCells.map(_.map(_.current.value).mkString(",")).mkString("\n")

  private def newEditable(value: String) =
    Editable(defaultValue = value)
      .withChildren(
        EditablePreview(),
        EditableInput()
      )

  def registerCsvEditorEventHandlers(controller: Controller[Boolean]) =
    controller
      .onClick(saveAndExit): event =>
        event.handled.withModel(true).terminate
      .onClick(exit): click =>
        click.handled.withModel(false).terminate
      .onChanged(tableCells.flatten*): event =>
        event.handled.withRenderChanges(status.withText(s"Changed a cell value to ${event.newValue}"))
