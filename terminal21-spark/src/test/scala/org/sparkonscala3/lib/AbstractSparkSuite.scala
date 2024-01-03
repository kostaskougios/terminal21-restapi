package org.sparkonscala3.lib

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers
import org.sparkonscala3.lib.util.Environment

import java.util.UUID

class AbstractSparkSuite extends AnyFunSuiteLike with Matchers:
  protected def randomString: String      = UUID.randomUUID().toString
  protected def randomTmpFilename: String = s"${Environment.tmpDirectory}/AbstractSparkSuite-" + UUID.randomUUID().toString
