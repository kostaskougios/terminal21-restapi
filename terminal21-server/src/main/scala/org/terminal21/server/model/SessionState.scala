package org.terminal21.server.model

import org.terminal21.model.CommandEvent
import org.terminal21.server.utils.NotificationRegistry

case class SessionState(
    json: String,
    eventsNotificationRegistry: NotificationRegistry[CommandEvent]
):
  def withNewState(newJson: String): SessionState = copy(json = newJson)
  def close: SessionState                         = copy(eventsNotificationRegistry = new NotificationRegistry[CommandEvent])
