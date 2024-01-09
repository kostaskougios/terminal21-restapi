package tests

import org.terminal21.client.components.nivo.*
import org.terminal21.client.*
import org.terminal21.client.components.*

@main def nivoComponents(): Unit =
  Sessions.withNewSession("nivo-components", "Nivo Components", NivoLib): session =>
    given ConnectedSession = session
    Seq(
      ResponsiveLine(
        data = Seq(
          Serie(
            "japan",
            data = Seq(
              Datum("plane", 262),
              Datum("helicopter", 26),
              Datum("boat", 43)
            )
          ),
          Serie(
            "france",
            "hsl(186, 70%, 50%)",
            Seq(
              Datum("plane", 271),
              Datum("helicopter", 31),
              Datum("boat", 27)
            )
          )
        ),
        yScale = Scale(stacked = Some(true))
      )
    ).render()
    session.waitTillUserClosesSession()
