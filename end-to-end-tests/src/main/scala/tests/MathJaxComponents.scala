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
      HStack().withChildren(
        Text(text = "Everyone knows this one"),
        MathJax(expression = """\(ax^2 + bx + c = 0\)""")
      ),
      MathJax(
        expression = """Does it align correctly? \(ax^2 + bx + c = 0\) It does provided CHTML renderer is used.""",
        style = Map("backgroundColor" -> "gray")
      )
    ).render()
    session.waitTillUserClosesSession()
