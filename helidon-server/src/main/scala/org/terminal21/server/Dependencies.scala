package org.terminal21.server

import functions.fibers.FiberExecutor
import org.terminal21.server.service.ServerSessionsServiceBeans
import org.terminal21.server.ui.UiWebSocketBeans

class Dependencies(fiberExecutor: FiberExecutor) extends ServerSessionsServiceBeans with UiWebSocketBeans(fiberExecutor)
