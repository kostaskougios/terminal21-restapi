package org.terminal21.client.collections

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class TypedMapTest extends AnyFunSuiteLike:
  object IntKey    extends TypedMapKey[Int]
  object StringKey extends TypedMapKey[String]

  test("add and get"):
    val m = TypedMap.empty + (IntKey -> 5) + (StringKey -> "x")
    m(IntKey) should be(5)
    m(StringKey) should be("x")

  test("getOrElse when key not available"):
    TypedMap.empty.getOrElse(IntKey, 2) should be(2)

  test("getOrElse when key available"):
    (TypedMap.empty + (IntKey -> 5)).getOrElse(IntKey, 2) should be(5)

  test("contains key positive"):
    (TypedMap.empty + (IntKey -> 5)).contains(IntKey) should be(true)

  test("contains key negative"):
    TypedMap.empty.contains(IntKey) should be(false)
