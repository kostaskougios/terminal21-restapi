package examples

import org.terminal21.client.json.chakra.{Box, ChakraProps}
import org.terminal21.client.json.{Header1, Paragraph}
import org.terminal21.client.{Chakra, Sessions, Std}

import java.util.UUID

@main def helloWorld(): Unit =
  val r = UUID.randomUUID().toString.substring(0, 4)
  Sessions.withNewSession(s"hello-world-$r", s"Hello World $r"): session =>
    println(session.session.id)

    val std    = session.use[Std]
    val chakra = session.use[Chakra]

    val h1 = Header1(key = "header", text = "Big news!")
    val p1 = Paragraph(key = "status", text = s"Hello there mr $r").withChildren(
      Box(text = "First box", props = ChakraProps(bg = "green", p = 4, color = "black")),
      Box(text = "Second box", props = ChakraProps(bg = "tomato", p = 4, color = "black"))
    )
    session.add(h1, p1)
    session.renderChanges()

    Thread.sleep(2000)
    p1.text = "Changed!"
    session.renderChanges()
//    std.header1("Big news!", key = "header")
//    std.paragraph(s"Hello there mr $r", key = "status")
//    chakra.button("Click Me!"): () =>
//      std.paragraph(s"Button clicked", key = "status")
//
//    chakra.box("This is a box", props = ChakraProps(bg = "green", p = 4, color = "black"))
//
//    for i <- 1 to 10 do
//      std.paragraph(s"$r = $i", key = "progress")
//      Thread.sleep(1000)
//
//    std.header1("Done", key = "header")
