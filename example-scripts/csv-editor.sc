#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A quick and dirty csv file editor for small csv files.
// Run with : ./csv-editor -- csv-file-path
// ------------------------------------------------------------------------------

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
  else "type,damage points,hit points\nmage,10dp,20hp\nwarrior,20dp,30hp" // a simple csv for demo purposes

val csv = toCsvModel(contents.split("\n").map(_.split(",").toSeq).toSeq)

Sessions
  .withNewSession(s"csv-editor-$fileName", s"CsvEdit: $fileName")
  .connect: session =>
    given ConnectedSession = session
    println(s"Now open ${session.uiUrl} to view the UI")
    val editor = new CsvEditorPage(csv)
    editor.run()

/** Our model. It stores the csv data as a Map of (x,y) coordinates -> value.
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

/** A nice approach to coding UI's is to create Page classes for every UI page. In this instance we need a page for our csv editor. The components function can
  * be easily tested if we want to test what is rendered and how it changes the model when events occur.
  */
class CsvEditorPage(initModel: CsvModel)(using session: ConnectedSession):

  val saveAndExit = Button("save-exit", text = "Save & Exit")
  val exit = Button("exit", text = "Exit Without Saving")

  def run(): Unit =
    for mv <- controller.render(initModel).iterator.lastOption.filter(_.model.save) // only save if model.save is true
    do save(mv.model)

  def editorComponent(model: CsvModel, events: Events): MV[CsvModel] =
    val tableCells =
      (0 until model.maxY).map: y =>
        (0 until model.maxX).map: x =>
          newEditable(x, y, model.csv(x, y))

    val newModel = tableCells.flatten.find(events.isChangedValue) match
      case Some(editable) =>
        val coords = editable.storedValue(CoordsKey)
        val newValue = events.changedValue(editable, "error")
        model.copy(csv = model.csv + (coords -> newValue), status = s"Changed value at $coords to $newValue")
      case None => model

    val view = QuickTable("csv-editor", variant = "striped", colorScheme = "teal", size = "mg")
      .withCaption("Please edit the csv contents above and click save to save and exit")
      .withRows(tableCells)

    MV(newModel, view)

  def components(model: CsvModel, events: Events): MV[CsvModel] =
    val cells = editorComponent(model, events)
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
      terminate = newModel.exitWithoutSave || newModel.save
    )

  def controller: Controller[CsvModel] = Controller(components)

  def save(model: CsvModel): Unit =
    val data = (0 until model.maxY).map: y =>
      (0 until model.maxX).map: x =>
        model.csv((x, y))
    FileUtils.writeStringToFile(file, data.map(_.mkString(",")).mkString("\n"), "UTF-8")
    println(s"Csv file saved to $file")

  object CoordsKey extends TypedMapKey[(Int, Int)]
  private def newEditable(x: Int, y: Int, value: String): Editable =
    Editable(s"cell-$x-$y", defaultValue = value) // note: anything receiving events should have a unique key, in this instance s"cell-$x-$y"
      .withChildren(
        EditablePreview(),
        EditableInput()
      )
      .store(
        CoordsKey,
        (x, y)
      ) // every UiElement has a store where we can store arbitrary data. Here we store the coordinates for the value this editable will edit
