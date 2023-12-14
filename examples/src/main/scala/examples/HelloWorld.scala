package examples

import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{ConnectedSession, Sessions}

@main def helloWorld(): Unit =
  Sessions.withNewSession("hello-world", "Hello World"): session =>
    given ConnectedSession = session
    println(session.session.id)

    val h1   = Header1(key = "header", text = "Welcome to the Hello World Program!")
    val b1   = Box(text = "First box", props = ChakraProps(bg = "green", p = 4, color = "black"))
    b1.withChildren(
      Button(text = "Click me!").onClick: () =>
        b1.text = "Clicked!"
        session.render()
    )
    val p1   = Paragraph(
      key = "status",
      text = s"Hello there mr X",
      children = Seq(
        b1,
        Box(text = "Second box", props = ChakraProps(bg = "tomato", p = 4, color = "black"))
      )
    )
    val grid = SimpleGrid(spacing = "8px", columns = 4)
    session.add(h1, p1, grid)
    session.render()

    for i <- 1 to 25 do
      Thread.sleep(1000)
      p1.text = s"i = $i"
      grid.addChildren(Box(text = s"counting i = $i", props = ChakraProps(bg = "green")))
      session.render()

    h1.text = "Done!"
    session.render()
