package org.terminal21.utils

import org.slf4j.Logger

class ErrorLogger(logger: Logger):
  def logErrors(f: => Unit): Unit =
    try f
    catch
      case t: Throwable =>
        logger.error("an error occurred", t)
        throw t
