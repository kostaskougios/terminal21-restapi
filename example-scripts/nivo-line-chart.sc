#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.fiberExecutor
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.nivo.*

import scala.util.Random
import NivoLineChart.*

Sessions.withNewSession("nivo-line-chart", "Nivo Line Chart", NivoLib /* note we need to register the NivoLib in order to use it */ ): session =>
  given ConnectedSession = session

  val chart = ResponsiveLine(
    data = createRandomData,
    yScale = Scale(stacked = Some(true)),
    axisBottom = Some(Axis(legend = "transportation", legendOffset = 36)),
    axisLeft = Some(Axis(legend = "count", legendOffset = -40)),
    legends = Seq(Legend())
  )

  Seq(
    Paragraph(text = "Means of transportation for various countries", style = Map("margin" -> 20)),
    chart
  ).render()

  fiberExecutor.submit:
    while !session.isClosed do
      Thread.sleep(2000)
      chart.withData(createRandomData).renderChanges()

  session.waitTillUserClosesSession()

object NivoLineChart:
  def createRandomData: Seq[Serie]    =
    Seq(
      dataFor("Japan"),
      dataFor("France"),
      dataFor("Greece"),
      dataFor("UK"),
      dataFor("Germany")
    )
  def dataFor(country: String): Serie =
    Serie(
      country,
      data = Seq(
        Datum("plane", rnd), // rnd = random int, see below
        Datum("helicopter", rnd),
        Datum("boat", rnd),
        Datum("car", rnd),
        Datum("submarine", rnd)
      )
    )

  def rnd = Random.nextInt(500) + 50
