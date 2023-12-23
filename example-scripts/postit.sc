#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// A note poster, where anyone can write a note
// ------------------------------------------------------------------------------

//> using dep io.github.kostaskougios::terminal21-ui-std:0.1

// always import these
import org.terminal21.client.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

Sessions.withNewSession("postit", "Post-It"): session =>
  given ConnectedSession = session

  val editor = Textarea(placeholder = "Please post your note by clicking here and editing the content")
  val messages=VStack()
  val add = Button(text = "Post It.").onClick: () =>
    // add the new msg.
    // note: editor.value is automatically updated by terminal-ui
    messages.addChildren(
      HStack(align = Some("stretch")).withChildren(
        Image(
          src = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_Notes_icon.svg/2048px-Apple_Notes_icon.svg.png",
          boxSize = Some("32px")
        ),
        Box(text = editor.value)
      )
    )
    // always render after adding/modifying something
    session.render()

  Seq(
    Paragraph(text = "Please type your note below and click 'Post It' to post it so that everyone can view it."),
    InputGroup().withChildren(
      InputLeftAddon().withChildren(EditIcon()),
      editor
    ),
    add,
    messages
  ).render()

  println(s"Now open ${session.uiUrl} to view the UI.")
  while true do Thread.sleep(100000)
