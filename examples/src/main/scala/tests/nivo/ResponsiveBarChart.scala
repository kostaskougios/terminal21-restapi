package tests.nivo

import org.terminal21.client.components.nivo.*
import tests.chakra.Common.commonBox

import scala.util.Random

object ResponsiveBarChart:
  def apply() = Seq(
    commonBox("ResponsiveBar"),
    ResponsiveBar(
      data = Seq(
        dataFor("AD"),
        dataFor("AE"),
        dataFor("GB"),
        dataFor("GR"),
        dataFor("IT"),
        dataFor("FR"),
        dataFor("GE"),
        dataFor("US")
      ),
      keys = Seq("hot dog", "burger", "sandwich", "kebab", "fries", "donut"),
      indexBy = "country",
      padding = 0.3,
      axisLeft = Some(Axis(legend = "food", legendOffset = -40)),
      axisBottom = Some(Axis(legend = "country", legendOffset = 32)),
      valueScale = Scale(`type` = "linear"),
      indexScale = Scale(`type` = "band", round = Some(true))
    )
  )

  def dataFor(country: String) =
    Seq(
      BarDatum("country", country),
      BarDatum("hot dog", Random.nextInt(500) + 50),
      BarDatum("hot dogColor", "hsl(202, 70%, 50%)"),
      BarDatum("burger", Random.nextInt(500) + 50),
      BarDatum("burgerColor", "hsl(106, 70%, 50%)"),
      BarDatum("sandwich", Random.nextInt(500) + 50),
      BarDatum("sandwichColor", "hsl(115, 70%, 50%)")
    )
