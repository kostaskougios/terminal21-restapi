package tests

import functions.fibers.FiberExecutor
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*

import java.util.concurrent.{CountDownLatch, TimeUnit}

@main def chakraComponents(): Unit =
  FiberExecutor.withFiberExecutor: executor =>
    Sessions.withNewSession("chakra-components", "Chakra Components"): session =>
      given ConnectedSession = session

      val latch     = new CountDownLatch(1)
      val box1      = Box(text = "First box", props = ChakraProps(bg = "green", p = 4, color = "black"))
      val editable1 = Editable(defaultValue = "Please type here")
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
        ,
        SimpleGrid(spacing = "8px", columns = 2).withChildren(
          Box(text = "Name"),
          editable1
        )
      ).render()

      executor.submit:
        while true do
          Thread.sleep(1000)
          println(s"editable value = ${editable1.value}")

      println("Waiting for button to be pressed for 1 hour")
      latch.await(1, TimeUnit.HOURS)
      Paragraph(text = "Exited").render()
