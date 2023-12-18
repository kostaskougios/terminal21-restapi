package org.terminal21.utils

import org.slf4j.Logger

import java.io.UncheckedIOException

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
