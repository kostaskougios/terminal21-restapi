#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.fiberExecutor
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.nivo.*

import scala.util.Random
import NivoBarChart.*
import org.terminal21.model.ClientEvent
// We don't have a model in this simple example, so we will import the standard Unit model
// for our controller to use.
import org.terminal21.client.Model.Standard.unitModel

Sessions
  .withNewSession("nivo-bar-chart", "Nivo Bar Chart")
  .andLibraries(NivoLib /* note we need to register the NivoLib in order to use it */ )
  .connect: session =>
    given ConnectedSession = session

    val chart = ResponsiveBar(
      data = createRandomData,
      keys = Seq("hot dog", "burger", "sandwich", "kebab", "fries", "donut"),
      indexBy = "country",
      padding = 0.3,
      defs = Seq(
        Defs("dots", "patternDots", "inherit", "#38bcb2", size = Some(4), padding = Some(1), stagger = Some(true)),
        Defs("lines", "patternLines", "inherit", "#eed312", rotation = Some(-45), lineWidth = Some(6), spacing = Some(10))
      ),
      fill = Seq(Fill("dots", Match("fries")), Fill("lines", Match("sandwich"))),
      axisLeft = Some(Axis(legend = "food", legendOffset = -40)),
      axisBottom = Some(Axis(legend = "country", legendOffset = 32)),
      valueScale = Scale(`type` = "linear"),
      indexScale = Scale(`type` = "band", round = Some(true)),
      legends = Seq(
        Legend(
          dataFrom = "keys",
          translateX = 120,
          itemsSpacing = 2,
          itemWidth = 100,
          itemHeight = 20,
          symbolSize = 20,
          symbolShape = "square"
        )
      )
    )

    // we'll send new data to our controller every 2 seconds via a custom event
    case class NewData(data: Seq[Seq[BarDatum]]) extends ClientEvent
    fiberExecutor.submit:
      while !session.isClosed do
        Thread.sleep(2000)
        session.fireEvent(NewData(createRandomData))

    Controller(
      Seq(
        Paragraph(text = "Various foods.", style = Map("margin" -> 20)),
        chart
      )
    ).render()
      .onEvent: // receive the new data and render them
        case ControllerClientEvent(handled, NewData(data)) =>
          handled.withRenderChanges(chart.withData(data))
      .handledEventsIterator
      .lastOption

object NivoBarChart:
  def createRandomData: Seq[Seq[BarDatum]] =
    Seq(
      dataFor("AD"),
      dataFor("AE"),
      dataFor("GB"),
      dataFor("GR"),
      dataFor("IT"),
      dataFor("FR"),
      dataFor("GE"),
      dataFor("US")
    )

  def dataFor(country: String) =
    Seq(
      BarDatum("country", country),
      BarDatum("hot dog", rnd),
      BarDatum("hot dogColor", "hsl(202, 70%, 50%)"),
      BarDatum("burger", rnd),
      BarDatum("burgerColor", "hsl(106, 70%, 50%)"),
      BarDatum("sandwich", rnd),
      BarDatum("sandwichColor", "hsl(115, 70%, 50%)"),
      BarDatum("kebab", rnd),
      BarDatum("kebabColor", "hsl(113, 70%, 50%)"),
      BarDatum("fries", rnd),
      BarDatum("friesColor", "hsl(209, 70%, 50%)"),
      BarDatum("donut", rnd),
      BarDatum("donutColor", "hsl(47, 70%, 50%)")
    )

  def rnd = Random.nextInt(500) + 50
