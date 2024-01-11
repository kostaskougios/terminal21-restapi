package tests.nivo

import org.terminal21.client.components.nivo.*
import tests.chakra.Common.commonBox

object ResponsiveBarChart:
  def apply() = Seq(
    commonBox("ResponsiveBar"),
    ResponsiveBar(
      data = Seq(
        Seq(
          BarDatum("country", "AD"),
          BarDatum("hot dog", 150),
          BarDatum("hot dogColor", "hsl(202, 70%, 50%)"),
          BarDatum("burger", 189),
          BarDatum("burgerColor", "hsl(106, 70%, 50%)"),
          BarDatum("sandwich", 170),
          BarDatum("sandwichColor", "hsl(115, 70%, 50%)")
        )
      ),
      keys = Seq("hot dog", "burger", "sandwich", "kebab", "fries", "donut"),
      valueScale = Scale(`type` = "linear"),
      indexScale = Scale(`type` = "band", round = Some(true))
    )
  )
