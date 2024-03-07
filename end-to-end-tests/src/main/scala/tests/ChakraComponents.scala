package tests

import org.terminal21.client.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import tests.chakra.*

@main def chakraComponents(): Unit =
  def loop(): Unit =
    println("Starting new session")
    Sessions
      .withNewSession("chakra-components", "Chakra Components")
      .connect: session =>
        given ConnectedSession = session

        def components(m: ChakraModel, events: Events): MV[ChakraModel] =
          // react tests reset the session to clear state
          val krButton = Button("reset", text = "Reset state")

          val bcs      = Buttons.components(m, events)
          val elements = Overlay.components(events) ++ Forms.components(
            m,
            events
          ) ++ Editables.components(
            events
          ) ++ Stacks.components ++ Grids.components ++ bcs.view ++ Etc.components ++ MediaAndIcons.components ++ DataDisplay.components ++ Typography.components ++ Feedback.components ++ Disclosure.components ++
            Navigation.components(events) ++ Seq(
              krButton
            )

          val modifiedModel = bcs.model
          val model         = modifiedModel.copy(
            rerun = events.isClicked(krButton)
          )
          MV(
            model,
            elements,
            model.rerun || model.terminate
          )

        Controller(components).render(ChakraModel()).iterator.lastOption.map(_.model) match
          case Some(m) if m.rerun =>
            Controller.noModel(Seq(Paragraph(text = "chakra-session-reset"))).render(())
            Thread.sleep(500)
            loop()
          case _                  =>

  loop()
