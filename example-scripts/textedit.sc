#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A text file editor for small files.
// ------------------------------------------------------------------------------

import org.apache.commons.io.FileUtils

import java.io.File

// always import these
import org.terminal21.client.*
import org.terminal21.client.components.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.std.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

import java.util.concurrent.CountDownLatch

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the file to edit"
  )

val fileName = args(0)
val file = new File(fileName)
val contents =
  if file.exists() then FileUtils.readFileToString(file, "UTF-8") else ""

def saveFile(content: String) =
  println(s"Saving file $fileName")
  FileUtils.writeStringToFile(file, content, "UTF-8")

Sessions
  .withNewSession(s"textedit-$fileName", s"Edit: $fileName")
  .connect: session =>
    given ConnectedSession = session

    case class Edit(content: String, savedContent: String, save: Boolean, exit: Boolean)
    // the main editor area.
    def components(edit: Edit, events: Events): MV[Edit] =
      val editorTextArea = Textarea(key = "editor", defaultValue = edit.content)
      // This will display a "saved" badge for a second when the user saves the file
      // This will display an asterisk when the contents of the file are changed in the editor
      val saveMenu = MenuItem("save-menu", text = "Save")
      val exitMenu = MenuItem("exit-menu", text = "Exit")
      val isSave = events.isClicked(saveMenu)
      val updatedContent = events.changedValue(editorTextArea, edit.content)
      val updatedEdit = edit.copy(
        content = updatedContent,
        save = isSave,
        savedContent = if isSave then updatedContent else edit.savedContent,
        exit = events.isClicked(exitMenu)
      )
      val modified = Badge(colorScheme = Some("red"), text = if updatedEdit.content != updatedEdit.savedContent then "*" else "")
      val status = Badge(text = if updatedEdit.save then "Saved" else "")

      val view = Seq(
        HStack().withChildren(
          Menu().withChildren(
            MenuButton("file-menu", text = "File").withChildren(ChevronDownIcon()),
            MenuList().withChildren(
              saveMenu,
              exitMenu
            )
          ),
          status,
          modified
        ),
        FormControl().withChildren(
          FormLabel(text = "Editor"),
          InputGroup().withChildren(
            InputLeftAddon().withChildren(EditIcon()),
            editorTextArea
          )
        )
      )

      MV(updatedEdit, view)

    println(s"Now open ${session.uiUrl} to view the UI")
    Controller(components)
      .render(Edit(contents, contents, false, false))
      .iterator
      .tapEach: mv =>
        if mv.model.save then saveFile(mv.model.content)
      .takeWhile(!_.model.exit)
      .foreach(_ => ())
    session.render(Seq(Paragraph(text = "Terminated")))
