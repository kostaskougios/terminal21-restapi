package org.terminal21.client.components

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.components.chakra.{Box, Text}
import org.scalatest.matchers.should.Matchers.*

class KeysTest extends AnyFunSuiteLike:
  test("doesn't reassign key to a defined key"):
    val b = Box(key = "k1")
    Keys.linearKeys(Seq(b)) should be(Seq(b))

  test("assign key"):
    val b = Box()
    Keys.linearKeys(Seq(b)) should be(Seq(b.withKey("k-0")))

  test("assign key to children"):
    val b = Box().withChildren(Text(), Text())
    Keys.linearKeys(Seq(b)) should be(
      Seq(
        b.withKey("k-0").withChildren(Text().withKey("k-0-0"), Text().withKey("k-0-1"))
      )
    )
