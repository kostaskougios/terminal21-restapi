package org.terminal21.server.utils

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class NotificationRegistryTest extends AnyFunSuiteLike:
  test("notifies"):
    var c = 0
    val n = new NotificationRegistry[Int]
    n.addAndNotify(5): i =>
      i should be(5)
      c += 1
      true

    c should be(1)

    n.addAndNotify(5): i =>
      i should be(5)
      c += 10
      true
    c should be(11)
    n.notifyAll(5) should be(2)
    c should be(22)

  test("removes notifier"):
    val n = new NotificationRegistry[Int]
    n.addAndNotify(1): _ =>
      false

    n.notifyAll(5) should be(0)
