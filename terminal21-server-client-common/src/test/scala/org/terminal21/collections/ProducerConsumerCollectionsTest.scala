package org.terminal21.collections

import functions.fibers.FiberExecutor
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*

class ProducerConsumerCollectionsTest extends AnyFunSuiteLike:
  val executor = FiberExecutor()
  test("producing/consuming"):
    val (it, producer) = ProducerConsumerCollections.lazyIterator[Int]()
    producer(2)
    it.take(1).toList should be(Seq(2))

  test("consuming blocks until an item is available"):
    val (it, producer) = ProducerConsumerCollections.lazyIterator[Int]()
    executor.submit:
      Thread.sleep(5)
      producer(10)

    it.take(1).toList should be(Seq(10))

  test("closing will throw an exception for consumers"):
    val (it, producer) = ProducerConsumerCollections.lazyIterator[Int]()
    val f                = executor.submit:
      an[NoSuchElementException] should be thrownBy it.toList

    producer(5)
    it.close()
    f.await()
