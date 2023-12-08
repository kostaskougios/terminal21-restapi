package org.terminal21.server

import org.terminal21.server.service.{ClientWebSocketBeans, ServerSessionsServiceBeans}
import org.terminal21.server.ui.{SessionsWebSocketBeans, TerminalWebSocketBeans}

class Dependencies extends ServerSessionsServiceBeans with TerminalWebSocketBeans with SessionsWebSocketBeans with ClientWebSocketBeans
