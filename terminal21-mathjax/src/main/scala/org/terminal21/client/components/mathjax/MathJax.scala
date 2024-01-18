package org.terminal21.client.components.mathjax

import org.terminal21.client.components.UiElement.HasStyle
import org.terminal21.client.components.{Keys, UiElement}

sealed trait MathJaxElement extends UiElement

/** see https://asciimath.org/ and https://github.com/fast-reflexes/better-react-mathjax
  */
case class MathJax(
    key: String = Keys.nextKey,
    // expression should be like """ text \( asciimath \) text""", i.e. """When \(a \ne 0\), there are two solutions to \(ax^2 + bx + c = 0\)"""
    expression: String = """fill in the expression as per https://asciimath.org/""",
    style: Map[String, Any] = Map.empty // Note: some of the styles are ignored by mathjax lib
) extends MathJaxElement
    with HasStyle[MathJax]:
  override def withStyle(v: Map[String, Any]): MathJax = copy(style = v)
  def withKey(k: String)                               = copy(key = k)
  def withExpression(e: String)                        = copy(expression = e)
