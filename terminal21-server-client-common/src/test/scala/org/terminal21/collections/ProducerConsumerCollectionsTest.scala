package org.terminal21.collections

import functions.fibers.FiberExecutor
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ProducerConsumerCollectionsTest extends AnyFunSuiteLike:
  val executor = FiberExecutor()
  test("producing/consuming"):
    val (list, producer) = ProducerConsumerCollections.lazyIterator[Int]()
    producer.put(2)
    list.take(1).toList should be(Seq(2))

  test("consuming blocks until an item is available"):
    val (list, producer) = ProducerConsumerCollections.lazyIterator[Int]()
    executor.submit:
      Thread.sleep(5)
      producer.put(10)

    list.take(1).toList should be(Seq(10))
