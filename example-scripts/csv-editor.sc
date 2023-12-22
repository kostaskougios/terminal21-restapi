//> using dep io.github.kostaskougios::terminal21-ui-std:0.1
//> using dep commons-io:commons-io:2.15.1
import org.apache.commons.io.FileUtils

import java.io.File
import scala.collection.concurrent.TrieMap

// always import these
import org.terminal21.client.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import java.util.concurrent.CountDownLatch

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the csv file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents =
  if file.exists() then FileUtils.readFileToString(file)
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

val exitLatch = new CountDownLatch(1)

Sessions.withNewSession(s"csv-editor-$fileName", s"CsvEdit: $fileName"):
  session =>
    given ConnectedSession = session

    val status = Box()
    val saveAndExit = Button(text = "Save & Exit")
      .onClick: () =>
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
          session.render()

    Seq(
      TableContainer().withChildren(
        Table(variant = "striped", colorScheme = Some("teal"), size = "mg")
          .withChildren(
            TableCaption(text =
              "Please edit the csv contents above and click save to save and exit"
            ),
            Thead(),
            Tbody(
              children = csv.zipWithIndex.map: (row, x) =>
                Tr(
                  children = row.zipWithIndex.map: (column, y) =>
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
    session.clear()
    session.render()
