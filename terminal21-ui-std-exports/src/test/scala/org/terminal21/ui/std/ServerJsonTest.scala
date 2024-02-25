package org.terminal21.ui.std

import io.circe.Json
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ServerJsonTest extends AnyFunSuiteLike:
  test("include"):
    val j1 = ServerJson(Seq("k1"), Map("k1" -> Json.fromInt(1), "k2" -> Json.fromInt(2), "k3" -> Json.fromInt(3)), Map("k1" -> Seq("k2", "k3"), "k2" -> Nil))
    val j2 = ServerJson(Seq("k2"), Map("k2" -> Json.fromInt(3)), Map("k2" -> Seq("k4")))
    j1.include(j2) should be(
      ServerJson(
        Seq("k1"),
        Map("k1" -> Json.fromInt(1), "k2" -> Json.fromInt(3), "k3" -> Json.fromInt(3)),
        Map("k1" -> Seq("k2", "k3"), "k2" -> Seq("k4"))
      )
    )

  test("include drops unused keys"):
    val j1 = ServerJson(
      Seq("root1"),
      Map("root1" -> Json.fromInt(1), "k2" -> Json.fromInt(2), "k2-c1" -> Json.fromInt(21), "k2-c1-c1" -> Json.fromInt(211)),
      Map("k1"    -> Seq("k2"), "k2"       -> Seq("k2-c1"), "k2-c1"    -> Seq("k2-c1-c1"), "k2-c1-c1"  -> Nil)
    )
    val j2 = ServerJson(Seq("k2"), Map("k2" -> Json.fromInt(3)), Map("k2" -> Nil))
    val r  = j1.include(j2)
    r.rootKeys should be(Seq("root1"))
    r.keyTree should be(Map("k1" -> Seq("k2"), "k2" -> Nil))
    r.elements should be(Map("root1" -> Json.fromInt(1), "k2" -> Json.fromInt(3)))
