package org.terminal21.client.components.mathjax

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiElement}

sealed trait MathJaxElement extends UiElement

case class MathJax(
    key: String = Keys.nextKey,
    @volatile var expression: String = "\\[\\sum_{n = 100}^{1000}\\left(\\frac{10\\sqrt{n}}{n}\\right)\\]",
    @volatile var style: Map[String, Any] = Map.empty // Note: some of the styles are ignored by mathjax lib
) extends MathJaxElement
    with HasStyle
