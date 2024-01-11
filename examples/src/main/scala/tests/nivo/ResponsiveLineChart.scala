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
      yScale = Scale(stacked = Some(true)),
      axisBottom = Some(Axis(legend = "transportation", legendOffset = 36)),
      axisLeft = Some(Axis(legend = "count", legendOffset = -40)),
      legends = Seq(
        Legend()
      )
    )
  )
