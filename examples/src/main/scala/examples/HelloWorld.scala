package examples

import org.terminal21.client.json.chakra.{Box, Button, ChakraProps}
import org.terminal21.client.json.{Header1, NewLine, Paragraph, Text}
import org.terminal21.client.{ConnectedSession, Sessions}

import java.util.UUID

@main def helloWorld(): Unit =
  val r = UUID.randomUUID().toString.substring(0, 4)
  Sessions.withNewSession(s"hello-world-$r", s"Hello World $r"): session =>
    given ConnectedSession = session
    println(session.session.id)

    val h1 = Header1(key = "header", text = "Welcome to the Hello World Program!")
    val b1 = Box(text = "First box", props = ChakraProps(bg = "green", p = 4, color = "black"))
    b1.withChildren(
      Button(text = "Click me!").onClick: () =>
        b1.text = "Clicked!"
        session.renderChanges()
    )
    val p1 = Paragraph(key = "status", text = s"Hello there mr $r").withChildren(
      b1,
      Box(text = "Second box", props = ChakraProps(bg = "tomato", p = 4, color = "black"))
    )
    val p2 = Paragraph()
    session.add(h1, p1, p2)
    session.renderChanges()

    for i <- 1 to 25 do
      Thread.sleep(1000)
      p1.text = s"i = $i"
      p2.addChildren(Text(text = s"counting i = $i"), NewLine())
      session.renderChanges()

    h1.text = "Done!"
    session.renderChanges()
