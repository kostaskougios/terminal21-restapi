package org.terminal21.server.ui

import org.slf4j.LoggerFactory

import java.io.UncheckedIOException

object DoWhileSessionOpen:
  private val logger = LoggerFactory.getLogger(getClass.getName)

  def returnTrueWhileSessionOpen(f: => Unit): Boolean =
    try
      f
      true
    catch
      case s: UncheckedIOException if s.getCause.getMessage == "Socket closed" =>
        logger.info("Socket closed")
        false
      // ignore
      case t: Throwable                                                        =>
        logger.error("An error occurred", t)
        false
