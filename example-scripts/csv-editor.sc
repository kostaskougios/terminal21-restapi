#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A quick and dirty csv file editor for small csv files.
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import org.apache.commons.io.FileUtils
import java.io.File
import java.util.concurrent.CountDownLatch
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

val csv = contents.split("\n").map(_.split(","))

// store the csv data in a more usable Map
val initialCsvMap = csv.zipWithIndex
  .flatMap: (row, y) =>
    row.zipWithIndex
      .map: (col, x) =>
        ((x, y), col)
  .toMap
val csvMap = TrieMap.empty[(Int, Int), String] ++ initialCsvMap

// save the map back to the csv file
def saveCsvMap() =
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

  // this will be countDown to 0 when we have to exit
val exitLatch = new CountDownLatch(1)

Sessions.withNewSession(s"csv-editor-$fileName", s"CsvEdit: $fileName"): session =>
  given ConnectedSession = session

  val status = Box()
  val saveAndExit = Button(text = "Save & Exit")
    .onClick: () =>
      saveCsvMap()
      session.add(Paragraph(text = "Csv file saved, exiting."))
      session.render()
      exitLatch.countDown()

  val exit = Button(text = "Exit Without Saving")
    .onClick: () =>
      exitLatch.countDown()

  def newEditable(x: Int, y: Int, value: String) =
    Editable(defaultValue = value)
      .withChildren(
        EditablePreview(),
        EditableInput()
      )
      .onChange: newValue =>
        csvMap((x, y)) = newValue
        status.text = s"($x,$y) value changed to $newValue"
        status.renderChanges()

  Seq(
    TableContainer().withChildren(
      Table(variant = "striped", colorScheme = Some("teal"), size = "mg")
        .withChildren(
          TableCaption(text = "Please edit the csv contents above and click save to save and exit"),
          Thead(),
          Tbody(
            children = csv.zipWithIndex.map: (row, y) =>
              Tr(
                children = row.zipWithIndex.map: (column, x) =>
                  Td().withChildren(newEditable(x, y, column))
              )
          )
        )
    ),
    HStack().withChildren(
      saveAndExit,
      exit,
      status
    )
  ).render()

  println(s"Now open ${session.uiUrl} to view the UI")
  // wait for one of the save/exit buttons to be pressed.
  exitLatch.await()
