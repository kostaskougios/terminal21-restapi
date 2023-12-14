package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.slf4j.LoggerFactory
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElementEncoding.uiElementEncoder
import org.terminal21.model.{CommandEvent, OnClick, Session}
import org.terminal21.ui.std.SessionsService

class ConnectedSession(val session: Session, sessionsService: SessionsService):
  private val logger   = LoggerFactory.getLogger(getClass)
  private var elements = List.empty[UiElement]

  def add(es: UiElement*): Unit =
    synchronized:
      elements = elements ::: es.toList

  private val eventHandlers = collection.concurrent.TrieMap.empty[String, EventHandler]

  def addEventHandler(key: String, handler: EventHandler): Unit =
    if eventHandlers.contains(key) then throw new IllegalArgumentException(s"There is already an event handler for $key")
    eventHandlers += key -> handler

  def fireEvent(event: CommandEvent): Unit =
    eventHandlers.get(event.key) match
      case Some(handler) =>
        (event, handler) match
          case (_: OnClick, h: OnClickEventHandler) => h.onClick()
          case x                                    => logger.error(s"Unknown event handling combination : $x")
      case None          =>
        logger.warn(s"There is no event handler for event $event")

  def renderChanges(): Unit =
    val j = toJson
    sessionsService.setSessionJsonState(session, j.toJson.noSpaces)

  private def toJson: JsonObject =
    val elementsCopy = synchronized(elements)
    val json         =
      for e <- elementsCopy
      yield e.asJson
    JsonObject(("elements", json.asJson))
