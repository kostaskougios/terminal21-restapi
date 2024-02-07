package org.terminal21.server

import functions.fibers.FiberExecutor
import org.terminal21.serverapp.ServerSideSessionsBeans
import org.terminal21.serverapp.bundled.AppManagerBeans

class Dependencies(val fiberExecutor: FiberExecutor) extends ServerBeans with ServerSideSessionsBeans with AppManagerBeans
