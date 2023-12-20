package tests

import functions.fibers.FiberExecutor
import org.terminal21.client.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.{Paragraph, render}
import tests.chakra.Forms

import java.util.concurrent.{CountDownLatch, TimeUnit}

@main def chakraComponents(): Unit =
  FiberExecutor.withFiberExecutor: executor =>
    Sessions.withNewSession("chakra-components", "Chakra Components"): session =>
      given ConnectedSession = session

      val latch      = new CountDownLatch(1)
      val greenProps = ChakraProps(bg = "green", p = 4, color = "black")
      val box1       = Box(text = "Simple grid", props = greenProps)
      val editable1  = Editable(defaultValue = "Please type here").withChildren(
        EditablePreview(),
        EditableInput()
      )
      editable1.onChange: newValue =>
        println(s"editable1 newValue = $newValue")
        println(editable1)
      val exitButton = Button(text = "Click to exit program", colorScheme = Some("red"))

      (Forms.components ++ Seq(
        box1,
        SimpleGrid(spacing = Some("8px"), columns = 4).withChildren(
          Box(text = "One", props = ChakraProps(bg = "yellow", color = "black")),
          Box(text = "Two", props = ChakraProps(bg = "tomato", color = "black")),
          Box(text = "Three", props = ChakraProps(bg = "blue", color = "black"))
        ),
        Box(text = "Buttons", props = greenProps),
        exitButton.onClick: () =>
          box1.text = "Exit Clicked!"
          exitButton.text = "Stopping..."
          exitButton.colorScheme = Some("green")
          session.render()
          Thread.sleep(1000)
          latch.countDown()
        ,
        Box(text = "Editables", props = greenProps),
        SimpleGrid(columns = 2).withChildren(
          Box(text = "Name"),
          editable1
        ),
        Box(text = "VStack", props = greenProps),
        VStack(spacing = Some("24px")).withChildren(
          Box(text = "1", props = ChakraProps(bg = "green", p = 2, color = "black")),
          Box(text = "2", props = ChakraProps(bg = "red", p = 2, color = "black")),
          Box(text = "3", props = ChakraProps(bg = "blue", p = 2, color = "black"))
        ),
        Box(text = "HStack", props = greenProps),
        HStack(spacing = Some("24px")).withChildren(
          Box(text = "1", props = ChakraProps(bg = "green", p = 2, color = "black")),
          Box(text = "2", props = ChakraProps(bg = "red", p = 2, color = "black")),
          Box(text = "3", props = ChakraProps(bg = "blue", p = 2, color = "black"))
        )
      )).render()

      println("Waiting for button to be pressed for 1 hour")
      latch.await(1, TimeUnit.HOURS)
      session.clear()
      Paragraph(text = "Terminated").render()
