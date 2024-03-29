#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A csv file viewer
// Run with: ./csv-viewer.sc -- csv-file
// ------------------------------------------------------------------------------

import org.terminal21.client.*
import org.terminal21.client.components.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import java.io.File
import org.apache.commons.io.FileUtils

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the csv file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents = FileUtils.readFileToString(file, "UTF-8")

val csv = contents.split("\n").map(_.split(","))

Sessions
  .withNewSession(s"csv-viewer-$fileName", s"CsvView: $fileName")
  .connect: session =>
    given ConnectedSession = session

    Controller
      .noModel(
        TableContainer() // We could use the QuickTable component here, but lets do it a bit more low level with the Chakra components
          .withChildren(
            Table(variant = "striped", colorScheme = Some("teal"), size = "mg")
              .withChildren(
                TableCaption(text = "Csv file contents"),
                Tbody(
                  children = csv.map: row =>
                    Tr(
                      children = row.map: column =>
                        Td(text = column)
                    )
                )
              )
          )
      )
      .render() // we don't have to process any events here, just let the user view the csv file.
    println(s"Now open ${session.uiUrl} to view the UI.")
    // since this is a read-only UI, we can exit the app but leave the session open on the UI for the user to examine the data.
    session.leaveSessionOpenAfterExiting()
