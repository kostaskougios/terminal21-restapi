package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

import java.util.concurrent.{CountDownLatch, TimeUnit}

@main def chakraComponents(): Unit =
  Sessions.withNewSession("chakra-components", "Chakra Components"): session =>
    given ConnectedSession = session

    val latch = new CountDownLatch(1)
    val box1  = Box(text = "First box", props = ChakraProps(bg = "green", p = 4, color = "black"))
    Seq(
      box1,
      SimpleGrid(spacing = "8px", columns = 4).withChildren(
        Box(text = "One", props = ChakraProps(bg = "green", color = "black")),
        Box(text = "Two", props = ChakraProps(bg = "tomato", color = "black")),
        Box(text = "Three", props = ChakraProps(bg = "blue", color = "black"))
      ),
      Button(text = "Exit Program").onClick: () =>
        box1.text = "Exit Clicked!"
        session.render()
        latch.countDown()
    ).render()

    println("Waiting for button to be pressed for 10 secs")
    latch.await(10, TimeUnit.SECONDS)
    Paragraph(text = "Exited").render()
