package tests.nivo

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.nivo.*
import tests.chakra.Common
import tests.chakra.Common.commonBox

import scala.collection.immutable.Seq

object ResponsiveLineChart:
  def apply() = Seq(
    commonBox("ResponsiveLine"),
    ResponsiveLine(
      data = Seq(
        dataFor("Japan"),
        dataFor("France"),
        dataFor("Greece"),
        dataFor("UK"),
        dataFor("Germany")
      ),
      yScale = Scale(stacked = Some(true)),
      axisBottom = Some(Axis(legend = "transportation", legendOffset = 36)),
      axisLeft = Some(Axis(legend = "count", legendOffset = -40)),
      legends = Seq(
        Legend()
      )
    )
  )

  def dataFor(country: String) =
    Serie(
      country,
      data = Seq(
        Datum("plane", rnd),
        Datum("helicopter", rnd),
        Datum("boat", rnd),
        Datum("car", rnd),
        Datum("submarine", rnd)
      )
    )
