package org.terminal21.client.components

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.components.chakra.{Box, Text}
import org.scalatest.matchers.should.Matchers.*

class UiElementTest extends AnyFunSuiteLike:
  test("flat"):
    val box = Box(key = "k1").withChildren(Text(key = "k2"), Text(key = "k3"))
    box.flat should be(
      Seq(box, Text(key = "k2"), Text(key = "k3"))
    )
  test("findKey"):
    Box(key = "k1").withChildren(Text(key = "k2"), Text(key = "k3")).findKey("k3") should be(Text(key = "k3"))
