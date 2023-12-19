package org.terminal21.server

import functions.fibers.FiberExecutor
import org.terminal21.server.service.{CommandWebSocketBeans, ServerSessionsServiceBeans}
import org.terminal21.server.ui.SessionsWebSocketBeans

class Dependencies(val fiberExecutor: FiberExecutor) extends ServerSessionsServiceBeans with SessionsWebSocketBeans with CommandWebSocketBeans
