package org.terminal21.server.model

import org.terminal21.model.CommandEvent
import org.terminal21.server.utils.NotificationRegistry
import org.terminal21.ui.std.ServerJson

case class SessionState(
    serverJson: ServerJson,
    eventsNotificationRegistry: NotificationRegistry[CommandEvent]
):
  def withNewState(newJson: ServerJson): SessionState = copy(serverJson = newJson)
  def close: SessionState                             = copy(eventsNotificationRegistry = new NotificationRegistry[CommandEvent])
