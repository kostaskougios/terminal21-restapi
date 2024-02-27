package tests

import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.*

import java.util.Date

@main def stateSessionStateBug(): Unit =
  Sessions
    .withNewSession("stale-session", "Stale Session")
    .connect: session =>
      given ConnectedSession = session
      import Model.Standard.unitModel

      val date       = new Date()
      val components = Seq(
        Paragraph(text = s"Now: $date"),
        QuickTable()
          .withHeaders("Title", "Value")
          .withRows(
            Seq(
              Seq(
                "Date - Editable",
                Editable(defaultValue = date.toString)
                  .withChildren(
                    EditablePreview(),
                    EditableInput()
                  )
              ),
              Seq(
                "Date - Input",
                Input(defaultValue = date.toString)
              ),
              Seq(
                "Date - Std Input",
                std.Input(defaultValue = date.toString)
              )
            )
          ),
        Button(text = "Close").onClick: event =>
          event.handled.terminate
      )
      Controller(components).render().handledEventsIterator.lastOption
