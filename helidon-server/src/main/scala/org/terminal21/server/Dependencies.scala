package org.terminal21.server

import functions.fibers.FiberExecutor
import org.terminal21.server.service.ServerSessionsServiceBeans
import org.terminal21.server.service.ui.StdUiImplBeans
import org.terminal21.server.ui.{SessionsWebSocketBeans, TerminalWebSocketBeans}

class Dependencies(fiberExecutor: FiberExecutor)
    extends ServerSessionsServiceBeans
    with TerminalWebSocketBeans(fiberExecutor)
    with SessionsWebSocketBeans(fiberExecutor)
    with StdUiImplBeans
