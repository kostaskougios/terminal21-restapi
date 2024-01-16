package org.terminal21.client.internal

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.components.Paragraph
import org.terminal21.client.components.chakra.Box
import org.scalatest.matchers.should.Matchers.*

class ElementTreeTest extends AnyFunSuiteLike:
  test("all elements"):
    val t   = new ElementTree
    val box = Box()
    val p   = Paragraph()
    t.add(Seq(box, p))
    t.allElements should be(Seq(box, p))

  test("all elements after clear"):
    val t   = new ElementTree
    val box = Box()
    val p   = Paragraph()
    t.add(Seq(box, p))
    t.clear()
    t.allElements should be(Nil)

  test("containsKey"):
    val t   = new ElementTree
    val box = Box()
    t.add(Seq(box))
    t.containsKey(box.key) should be(true)

  test("containsKey deep"):
    val t   = new ElementTree
    val p   = Paragraph()
    val box = Box().withChildren(p)
    t.add(Seq(box))
    t.containsKey(p.key) should be(true)

  test("containsKey after clear"):
    val t   = new ElementTree
    val box = Box()
    t.add(Seq(box))
    t.clear()
    t.containsKey(box.key) should be(false)
