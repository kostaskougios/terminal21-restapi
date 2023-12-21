package org.terminal21.utils

import org.slf4j.Logger

import java.io.UncheckedIOException
import scala.annotation.tailrec

class ErrorLogger(logger: Logger):
  def logErrors(f: => Unit): Unit =
    try f
    catch
      case s: UncheckedIOException if s.getCause.getMessage == "Socket closed" =>
        logger.info("Socket closed")
        throw s
      case t: Throwable                                                        =>
        logger.error("an error occurred", t)
        throw t

  @tailrec final def tryForeverLogErrors[R](f: => R): R =
    try f
    catch
      case t: Throwable =>
        logger.error("An error occurred but will retry forever until there is no error", t)
        tryForeverLogErrors(f)
