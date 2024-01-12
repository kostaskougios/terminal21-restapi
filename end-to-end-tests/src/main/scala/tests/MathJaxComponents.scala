package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.mathjax.*

@main def mathJaxComponents(): Unit =
  Sessions.withNewSession("mathjax-components", "MathJax Components", MathJaxLib): session =>
    given ConnectedSession = session
    Seq(
      HStack().withChildren(
        Text(text = "Lets write some math expressions that will wow everybody!"),
        MathJax(expression = """\[\sum_{n = 200}^{1000}\left(\frac{20\sqrt{n}}{n}\right)\]""")
      ),
      MathJax(expression = """Everyone knows this one : \(ax^2 + bx + c = 0\). But how about this? \(\sum_{i=1}^n i^3 = ((n(n+1))/2)^2 \)"""),
      MathJax(
        expression = """Does it align correctly? \(ax^2 + bx + c = 0\) It does provided CHTML renderer is used.""",
        style = Map("backgroundColor" -> "gray")
      )
    ).render()
    session.waitTillUserClosesSession()
