package org.terminal21.client.components

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.chakra.{Box, QuickTable, Text}
import org.terminal21.client.components.std.Paragraph

class UiElementTest extends AnyFunSuiteLike:
  test("flat"):
    val box = Box(key = "k1").withChildren(Text(key = "k2"), Text(key = "k3"))
    box.flat should be(
      Seq(box, Text(key = "k2"), Text(key = "k3"))
    )
  test("findKey"):
    Box(key = "k1").withChildren(Text(key = "k2"), Text(key = "k3")).findKey("k3") should be(Text(key = "k3"))

  test("substituteComponents when not component"):
    val e = Text()
    e.substituteComponents should be(e)

  test("substituteComponents when component"):
    val e = QuickTable(key = "k1")
    e.substituteComponents should be(Box("k1", children = e.rendered))

  test("substituteComponents when children are component"):
    val t = QuickTable(key = "k1")
    val e = Paragraph(key = "p1").withChildren(t)
    e.substituteComponents should be(Paragraph(key = "p1").withChildren(Box("k1", children = t.rendered)))
