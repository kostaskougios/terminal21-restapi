package org.terminal21.client.json

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.components.chakra.Button
import org.scalatest.matchers.should.Matchers.*

class UiElementEncodingTest extends AnyFunSuiteLike:
  val encoding = new UiElementEncoding(Seq(StdElementEncoding))
  test("dataStore"):
    val b = Button()
    val j = encoding.uiElementEncoder(b).deepDropNullValues
    j.hcursor.downField("Button").downField("dataStore").failed should be(true)
