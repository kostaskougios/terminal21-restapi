package org.terminal21.server

import functions.fibers.FiberExecutor
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessionsBeans}
import org.terminal21.serverapp.bundled.AppManagerBeans

class Dependencies(val fiberExecutor: FiberExecutor, val apps: Seq[ServerSideApp]) extends ServerBeans with ServerSideSessionsBeans with AppManagerBeans:
  override def dependencies: Dependencies = this
