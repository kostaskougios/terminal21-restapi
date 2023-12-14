package tests

import functions.fibers.FiberExecutor
import org.terminal21.client.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.{Paragraph, render}

import java.util.concurrent.{CountDownLatch, TimeUnit}

@main def chakraComponents(): Unit =
  FiberExecutor.withFiberExecutor: executor =>
    Sessions.withNewSession("chakra-components", "Chakra Components"): session =>
      given ConnectedSession = session

      val latch     = new CountDownLatch(1)
      val box1      = Box(text = "First box", props = ChakraProps(bg = "green", p = 4, color = "black"))
      val editable1 = Editable(defaultValue = "Please type here")
      editable1.onChange: newValue =>
        println(s"editable1 newValue = $newValue")
        println(editable1)
      val email     = Input(`type` = "email")
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
        ),
        Box(text = "VStack", props = ChakraProps(bg = "green", color = "black")),
        VStack(spacing = "24px").withChildren(
          Box(text = "1", props = ChakraProps(bg = "green", p = 2, color = "black")),
          Box(text = "2", props = ChakraProps(bg = "red", p = 2, color = "black")),
          Box(text = "3", props = ChakraProps(bg = "blue", p = 2, color = "black"))
        ),
        Box(text = "HStack", props = ChakraProps(bg = "green", color = "black")),
        HStack(spacing = "24px").withChildren(
          Box(text = "1", props = ChakraProps(bg = "green", p = 2, color = "black")),
          Box(text = "2", props = ChakraProps(bg = "red", p = 2, color = "black")),
          Box(text = "3", props = ChakraProps(bg = "blue", p = 2, color = "black"))
        ),
        Box(text = "And now a Form", props = ChakraProps(bg = "green", color = "black")),
        FormControl().withChildren(
          FormLabel(text = "Email address"),
          email,
          FormHelperText(text = "We'll never share your email.")
        )
      ).render()

      executor.submit:
        while true do
          Thread.sleep(1000)
          println(s"editable value = ${editable1.value}, email = ${email.value}")

      println("Waiting for button to be pressed for 1 hour")
      latch.await(1, TimeUnit.HOURS)
      session.clear()
      Paragraph(text = s"Terminated with editable = ${editable1.value} and email = ${email.value}").render()
