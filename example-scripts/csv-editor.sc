#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A quick and dirty csv file editor for small csv files.
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.collections.TypedMapKey
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

val csv = toCsvModel(contents.split("\n").map(_.split(",").toSeq).toSeq)

Sessions
  .withNewSession(s"csv-editor-$fileName", s"CsvEdit: $fileName")
  .connect: session =>
    given ConnectedSession = session
    println(s"Now open ${session.uiUrl} to view the UI")
    val editor = new CsvEditor(csv)
    editor.run()

/** Our model. If the user clicks "Save", we'll set `save` to true and store the csv data into `csv`
  */
case class CsvModel(save: Boolean, exitWithoutSave: Boolean, csv: Map[(Int, Int), String], maxX: Int, maxY: Int, status: String = "Please edit the file.")
def toCsvModel(csv: Seq[Seq[String]]) =
  val maxX = csv.map(_.size).max
  val maxY = csv.size
  val m = csv.zipWithIndex
    .flatMap: (row, y) =>
      row.zipWithIndex.map: (column, x) =>
        ((x, y), column)
    .toMap
  CsvModel(false, false, m, maxX, maxY)

class CsvEditor(initModel: CsvModel)(using session: ConnectedSession):

  val saveAndExit = Button(key = "save-exit", text = "Save & Exit")
  val exit = Button(key = "exit", text = "Exit Without Saving")

  def run(): Unit =
    for mv <- controller.render(initModel).iterator.lastOption.filter(_.model.save) // only save if model.save is true
    do save(mv.model)

  def cellsComponent(model: CsvModel, events: Events): MV[CsvModel] =
    val tableCells =
      (0 until model.maxY).map: y =>
        (0 until model.maxX).map: x =>
          newEditable(x, y, model.csv(x, y))

    val view = QuickTable(variant = "striped", colorScheme = "teal", size = "mg")
      .withCaption("Please edit the csv contents above and click save to save and exit")
      .withRows(tableCells)

    tableCells.flatten.find(events.isChangedValue) match
      case Some(editable) =>
        val coords = editable.dataStore(CoordsKey)
        val newValue = events.changedValue(editable, "error")
        MV(
          model.copy(csv = model.csv + (coords -> newValue), status = s"Changed value at $coords to $newValue"),
          view
        )
      case None => MV(model, view)

  def components(model: CsvModel, events: Events): MV[CsvModel] =
    val cells = cellsComponent(model, events)
    val newModel = cells.model.copy(
      save = events.isClicked(saveAndExit),
      exitWithoutSave = events.isClicked(exit)
    )
    val view = cells.view ++ Seq(
      HStack().withChildren(
        saveAndExit,
        exit,
        Box(text = newModel.status)
      )
    )
    MV(
      newModel,
      view,
      isTerminate = events.isClicked(saveAndExit) || events.isClicked(exit)
    )

  /** @return
    *   true if the user clicked "Save", false if the user clicked "Exit" or closed the session
    */
  def controller: Controller[CsvModel] =
    Controller(components)

  def save(model: CsvModel): Unit =
    val data = (0 until model.maxY).map: y =>
      (0 until model.maxX).map: x =>
        model.csv((x, y))
    FileUtils.writeStringToFile(file, data.map(_.mkString(",")).mkString("\n"), "UTF-8")
    println(s"Csv file saved to $file")

  object CoordsKey extends TypedMapKey[(Int, Int)]
  private def newEditable(x: Int, y: Int, value: String): Editable =
    Editable(key = s"cell-$x-$y", defaultValue = value)
      .withChildren(
        EditablePreview(),
        EditableInput()
      )
      .store(CoordsKey, (x, y))
