package tests.chakra

import org.terminal21.client.components.chakra.Box

object Common:
  def commonBox(text: String) = Box(text = text, bg = "green", p = 4, color = "black")
