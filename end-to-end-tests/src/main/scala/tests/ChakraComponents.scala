package tests

import cats.conversions.all.autoConvertProfunctorVariance
import org.terminal21.client.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.*

@main def chakraComponents(): Unit =
  def loop(): Unit =
    println("Starting new session")
    Sessions
      .withNewSession("chakra-components", "Chakra Components")
      .connect: session =>
        given ConnectedSession          = session
        given model: Model[ChakraModel] = Model(ChakraModel())

        // react tests reset the session to clear state
        val krButton = Button(text = "Reset state").onClick: event =>
          event.handled.withModel(_.copy(rerun = true)).terminate

        def components(m: ChakraModel): Seq[UiElement] =
          Overlay.components(
            m
          ) ++ Forms.components(
            m
          ) ++ Editables.components(
            m
          ) ++ Stacks.components ++ Grids.components ++ Buttons.components ++ Etc.components ++ MediaAndIcons.components ++ DataDisplay.components ++ Typography.components ++ Feedback.components ++ Disclosure.components ++
            Navigation.components(m) ++ Seq(
              krButton
            )
        Controller(components).render().handledEventsIterator.lastOption.map(_.model) match
          case Some(m) if m.rerun =>
            Controller.noModel(Seq(Paragraph(text = "chakra-session-reset"))).render()
            Thread.sleep(500)
            loop()
          case _                  =>

  loop()
