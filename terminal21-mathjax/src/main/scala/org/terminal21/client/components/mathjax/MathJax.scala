package org.terminal21.client.components.mathjax

import org.terminal21.client.components.{Keys, UiElement}

sealed trait MathJaxElement extends UiElement

case class MathJax(key: String = Keys.nextKey, expression: String = "\\[\\sum_{n = 100}^{1000}\\left(\\frac{10\\sqrt{n}}{n}\\right)\\]") extends MathJaxElement
