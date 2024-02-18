package tests

import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.{ConnectedSession, Sessions}

import java.util.Date

@main def stateSessionStateBug(): Unit =
  Sessions
    .withNewSession("state-session", "Stale Session")
    .connect: session =>
      given ConnectedSession = session

      var exitFlag = false
      val date     = new Date()
      Seq(
        Paragraph(text = s"Now: $date"),
        QuickTable()
          .headers("Title", "Value")
          .rows(
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
                std.Input(defaultValue = Some(date.toString))
              )
            )
          ),
        Button(text = "Close").onClick: () =>
          exitFlag = true
      ).render()
      session.waitTillUserClosesSessionOr(exitFlag)
