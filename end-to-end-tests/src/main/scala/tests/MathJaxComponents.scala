package tests

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.mathjax.*

@main def mathJaxComponents(): Unit =
  Sessions.withNewSession("mathjax-components", "MathJax Components", MathJaxLib): session =>
    given ConnectedSession = session
    Seq(MathJax(expression = "\\[\\sum_{n = 200}^{1000}\\left(\\frac{10\\sqrt{n}}{n}\\right)\\]")).render()
    session.waitTillUserClosesSession()
