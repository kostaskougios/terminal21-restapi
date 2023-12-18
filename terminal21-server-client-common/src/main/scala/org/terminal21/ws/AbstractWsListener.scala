package org.terminal21.ws

import io.helidon.websocket.WsListener
import org.slf4j.LoggerFactory
import org.terminal21.utils.ErrorLogger

import java.io.UncheckedIOException

abstract class AbstractWsListener extends WsListener:
  protected val logger      = LoggerFactory.getLogger(getClass)
  protected val errorLogger = new ErrorLogger(logger)

  protected def tryOnSocketClosedIgnore(f: => Unit): Unit =
    try f
    catch case _: UncheckedIOException => () // nop

