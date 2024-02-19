#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A quick and dirty csv file editor for small csv files.
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*

import java.util.concurrent.atomic.AtomicBoolean
import org.terminal21.client.components.*
import org.terminal21.client.model.*
import org.terminal21.model.*
import org.terminal21.model.SessionOptions
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import org.apache.commons.io.FileUtils
import java.io.File
import scala.collection.concurrent.TrieMap

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the csv file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents =
  if file.exists() then FileUtils.readFileToString(file, "UTF-8")
  else "type,damage points,hit points\nmage,10dp,20hp\nwarrior,20dp,30hp"

println(s"Contents: $contents")
val csv = contents.split("\n").map(_.split(","))

// store the csv data in a more usable Map
val initialCsvMap = csv.zipWithIndex
  .flatMap: (row, y) =>
    row.zipWithIndex
      .map: (col, x) =>
        ((x, y), col)
  .toMap

type Coords = (Int, Int)
type CsvMap = Map[Coords, String]
// save the map back to the csv file
def saveCsvMap(csvMap: CsvMap): Unit =
  val coords = csvMap.keySet
  val maxX = coords.map(_._1).max
  val maxY = coords.map(_._2).max

  val s = (0 to maxX)
    .map: y =>
      (0 to maxY)
        .map: x =>
          csvMap.getOrElse((x, y), "")
        .mkString(",")
    .mkString("\n")
  FileUtils.writeStringToFile(file, s, "UTF-8")

Sessions
  .withNewSession(s"csv-editor-$fileName", s"CsvEdit: $fileName")
  .connect: session =>
    given ConnectedSession = session

    val status = Box()
    val saveAndExit = Button(text = "Save & Exit")
    val exit = Button(text = "Exit Without Saving")

    def newEditable(value: String) =
      Editable(defaultValue = value)
        .withChildren(
          EditablePreview(),
          EditableInput()
        )

    case class Cell(coords: Coords, editable: Editable)
    val tableCells = csv.zipWithIndex.map: (row, y) =>
      row.zipWithIndex.map: (column, x) =>
        Cell((x, y), newEditable(column))

    Seq(
      QuickTable(variant = "striped", colorScheme = "teal", size = "mg")
        .withCaption("Please edit the csv contents above and click save to save and exit")
        .withRows(
          tableCells.toSeq.map: rowCells =>
            rowCells.map(_.editable)
        ),
      HStack().withChildren(
        saveAndExit,
        exit,
        status
      )
    ).render()

    println(s"Now open ${session.uiUrl} to view the UI")

    val editableToCoords = tableCells.flatten.map(cell => (cell.editable.key, cell.coords)).toMap

    case class EditorState(saveAndExitClicked: Boolean, exitWithoutSavingClicked: Boolean, csvMap: CsvMap, changed: Option[Coords]):
      def terminated = saveAndExitClicked || exitWithoutSavingClicked

    session.eventIterator
      .scanLeft(EditorState(false, false, csvMap = initialCsvMap, None)):
        case (state, UiEvent(OnChange(key, value), receivedBy)) if editableToCoords.contains(key) =>
          val coords = editableToCoords(key)
          state.copy(csvMap = state.csvMap + (coords -> value), changed = Some(coords))
        case (state, event) => state.copy(saveAndExitClicked = event.isTarget(saveAndExit), exitWithoutSavingClicked = event.isTarget(exit), changed = None)
      .tapEach: state =>
        for coords <- state.changed do status.withText(s"Changed value at $coords, new value is ${state.csvMap(coords)}").renderChanges()
      .dropWhile(!_.terminated)
      .take(1)
      .filter(_.saveAndExitClicked)
      .foreach: state =>
        saveCsvMap(state.csvMap)
        status.withText("Csv file saved, exiting.").renderChanges()
        Thread.sleep(1000)
