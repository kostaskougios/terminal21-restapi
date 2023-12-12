package org.terminal21.server

import org.terminal21.server.service.{CommandWebSocketBeans, ServerSessionsServiceBeans}
import org.terminal21.server.ui.SessionsWebSocketBeans

class Dependencies extends ServerSessionsServiceBeans with SessionsWebSocketBeans with CommandWebSocketBeans
