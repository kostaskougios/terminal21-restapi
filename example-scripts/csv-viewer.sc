// ------------------------------------------------------------------------------
// A csv file viewer
// ------------------------------------------------------------------------------

//> using dep io.github.kostaskougios::terminal21-ui-std:0.1
//> using dep commons-io:commons-io:2.15.1

// always import these
import org.terminal21.client.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.apache.commons.io.FileUtils
import org.terminal21.client.components.chakra.*

import java.io.File
import java.util.concurrent.CountDownLatch
import scala.collection.concurrent.TrieMap

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the csv file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents = FileUtils.readFileToString(file)

val csv = contents.split("\n").map(_.split(","))

Sessions.withNewSession(s"csv-viewer-$fileName", s"CsvView: $fileName"):
  session =>
    given ConnectedSession = session

    Seq(
      TableContainer().withChildren(
        Table(variant = "striped", colorScheme = Some("teal"), size = "mg")
          .withChildren(
            TableCaption(text = "Csv file contents"),
            Thead(),
            Tbody(
              children = csv.map: row =>
                Tr(
                  children = row.map: column =>
                    Td(text = column)
                )
            )
          )
      )
    ).render()
    println(s"Now open ${session.uiUrl} to view the UI.")
    session.waitTillUserClosesSession()
