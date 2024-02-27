#!/usr/bin/env -S scala-cli project.scala
// ------------------------------------------------------------------------------
// A note poster, where anyone can write a note
// ------------------------------------------------------------------------------

// always import these
import org.terminal21.client.*
import org.terminal21.client.components.*
// std components, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.std.*
// use the chakra components for menus, forms etc, https://chakra-ui.com/docs/components
// The scala case classes : https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/chakra/ChakraElement.scala
import org.terminal21.client.components.chakra.*

Sessions
  .withNewSession("postit", "Post-It")
  .connect: session =>
    given ConnectedSession = session
    println(s"Now open ${session.uiUrl} to view the UI.")
    new PostItPage().run()

class PostItPage(using ConnectedSession):
  import org.terminal21.client.Model.Standard.unitModel // we won't use a model for this one
  def run(): Unit = controller.render().handledEventsIterator.lastOption

  def components: Seq[UiElement] =
    val editor = Textarea(placeholder = "Please post your note by clicking here and editing the content")
    val messages = VStack(align = Some("stretch"))
    val add = Button(text = "Post It.").onClick: event =>
      import event.*
      // add the new msg.
      // note: editor.value is automatically updated by terminal-ui
      val currentMessages = messages.current
      handled.withRenderChanges(
        currentMessages
          .addChildren(
            HStack().withChildren(
              Image(
                src = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_Notes_icon.svg/2048px-Apple_Notes_icon.svg.png",
                boxSize = Some("32px")
              ),
              Box(text = editor.current.value)
            )
          )
      )
    Seq(
      Paragraph(text = "Please type your note below and click 'Post It' to post it so that everyone can view it."),
      InputGroup().withChildren(
        InputLeftAddon().withChildren(EditIcon()),
        editor
      ),
      add,
      messages
    )

  def controller = Controller(components)
