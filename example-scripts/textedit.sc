//> using dep io.github.kostaskougios::terminal21-ui-std:0.1

import org.terminal21.client.*
import org.terminal21.client.components.*

// use the chakra components, https://chakra-ui.com/docs/components
import org.terminal21.client.components.chakra.*

import java.util.concurrent.CountDownLatch

if args.length != 1 then
  throw new IllegalArgumentException(
    "Expecting 1 argument, the name of the file to edit"
  )

val fileName = args(0)

Sessions.withNewSession(s"textedit-$fileName", s"Edit: $fileName"): session =>
  given ConnectedSession = session
  val exitLatch = new CountDownLatch(1)
  Seq(
    Menu().withChildren(
      MenuButton(
        text = "File",
        size = Some("sm"),
        colorScheme = Some("teal")
      ).withChildren(
        ChevronDownIcon()
      )
    )
  ).render()

  println(s"Now open ${session.uiUrl} to view the UI")
  exitLatch.await()
  session.clear()
  Paragraph(text = "Terminated").render()

