package org.terminal21.client.model

import org.terminal21.client.components.UiElement
import org.terminal21.model.CommandEvent

case class GlobalEvent(event: CommandEvent, receivedBy: UiElement)
