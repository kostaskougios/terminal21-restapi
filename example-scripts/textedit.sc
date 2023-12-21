//> using dep io.github.kostaskougios::terminal21-ui-std:0.1
//> using dep commons-io:commons-io:2.15.1

import org.terminal21.client.*
import org.terminal21.client.components.*

import java.io.File

// use the chakra components, https://chakra-ui.com/docs/components
import org.apache.commons.io.FileUtils
import org.terminal21.client.components.chakra.*

import java.util.concurrent.CountDownLatch

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents =
  if file.exists() then FileUtils.readFileToString(file) else ""

def saveFile(content:String) = FileUtils.writeStringToFile(file,content)
    
Sessions.withNewSession(s"textedit-$fileName", s"Edit: $fileName"): session =>
  given ConnectedSession = session
  val exitLatch = new CountDownLatch(1)

  val editor = Textarea(value = contents)
  Seq(
    Menu().withChildren(
      MenuButton(text = "File").withChildren(ChevronDownIcon()),
      MenuList().withChildren(
        MenuItem(text = "Save")
          .onClick: () =>
            saveFile(editor.value),
        MenuItem(text = "Exit")
          .onClick: () =>
            exitLatch.countDown()
      )
    ),
    FormControl().withChildren(
        FormLabel(text = "Editor"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(EditIcon()),
          editor
        )
      )
  ).render()

  println(s"Now open ${session.uiUrl} to view the UI")
  exitLatch.await()
  session.clear()
  Paragraph(text = "Terminated").render()
