package org.terminal21.server

import functions.fibers.FiberExecutor
import org.terminal21.server.service.ServerSessionsServiceBeans
import org.terminal21.server.service.ui.StdUiImplBeans
import org.terminal21.server.ui.{SessionsWebSocketBeans, UiWebSocketBeans}

class Dependencies(fiberExecutor: FiberExecutor)
    extends ServerSessionsServiceBeans
    with UiWebSocketBeans(fiberExecutor)
    with SessionsWebSocketBeans(fiberExecutor)
    with StdUiImplBeans
