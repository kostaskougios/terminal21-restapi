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
              StringIntDatum("plane", 262),
              StringIntDatum("helicopter", 26),
              StringIntDatum("boat", 43)
            )
          )
        )
      )
    ).render()
    session.waitTillUserClosesSession()
