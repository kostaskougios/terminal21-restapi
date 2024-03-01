package org.terminal21.collections

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class TypedMapTest extends AnyFunSuiteLike:
  object IntKey    extends TypedMapKey[Int]
  object StringKey extends TypedMapKey[String]

  test("apply"):
    val m = TypedMap.empty + (IntKey -> 5) + (StringKey -> "x")
    m(IntKey) should be(5)
    m(StringKey) should be("x")

  test("construct"):
    val m = TypedMap(IntKey -> 5, StringKey -> "x")
    m(IntKey) should be(5)
    m(StringKey) should be("x")

  test("keys"):
    val m = TypedMap(IntKey -> 5, StringKey -> "x")
    m.keys.toSet should be(Set(IntKey, StringKey))

  test("get"):
    val m = TypedMap.empty + (IntKey -> 5) + (StringKey -> "x")
    m.get(IntKey) should be(Some(5))
    m.get(StringKey) should be(Some("x"))

  test("getOrElse when key not available"):
    TypedMap.empty.getOrElse(IntKey, 2) should be(2)

  test("getOrElse when key available"):
    (TypedMap.empty + (IntKey -> 5)).getOrElse(IntKey, 2) should be(5)

  test("contains key positive"):
    (TypedMap.empty + (IntKey -> 5)).contains(IntKey) should be(true)

  test("contains key negative"):
    TypedMap.empty.contains(IntKey) should be(false)

  test("get key negative"):
    TypedMap.empty.get(IntKey) should be(None)

  test("equals positive"):
    val m1 = TypedMap.empty + (IntKey -> 5)
    val m2 = TypedMap.empty + (IntKey -> 5)
    m1 should be(m2)

  test("equals negative"):
    val m1 = TypedMap.empty + (IntKey -> 5)
    val m2 = TypedMap.empty + (IntKey -> 6)
    m1 should not be m2

  test("hashCode positive"):
    val m1 = TypedMap.empty + (IntKey -> 5)
    val m2 = TypedMap.empty + (IntKey -> 5)
    m1.hashCode should be(m2.hashCode)

  test("hashCode negative"):
    val m1 = TypedMap.empty + (IntKey -> 5)
    val m2 = TypedMap.empty + (IntKey -> 6)
    m1.hashCode should not be m2.hashCode
