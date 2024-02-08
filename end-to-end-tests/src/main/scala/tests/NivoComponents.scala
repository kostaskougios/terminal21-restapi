package tests

import org.terminal21.client.components.nivo.*
import org.terminal21.client.*
import org.terminal21.client.components.*
import tests.nivo.{ResponsiveBarChart, ResponsiveLineChart}

@main def nivoComponents(): Unit =
  Sessions
    .withNewSession("nivo-components", "Nivo Components")
    .andLibraries(NivoLib)
    .connect: session =>
      given ConnectedSession = session
      (ResponsiveBarChart() ++ ResponsiveLineChart()).render()
      session.waitTillUserClosesSession()
