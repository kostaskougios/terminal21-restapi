package org.terminal21.ui.std

import io.circe.Json
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ServerJsonTest extends AnyFunSuiteLike:
  test("include"):
    val j1 = ServerJson(Seq("k1"), Map("k1" -> Json.fromInt(1), "k2" -> Json.fromInt(2)), Map("k1" -> Seq("k2", "k3")))
    val j2 = ServerJson(Nil, Map("k2" -> Json.fromInt(3)), Map("k2" -> Seq("k4")))
    j1.include(j2) should be(
      ServerJson(
        Seq("k1"),
        Map("k1" -> Json.fromInt(1), "k2" -> Json.fromInt(3)),
        Map("k1" -> Seq("k2", "k3"), "k2" -> Seq("k4"))
      )
    )
