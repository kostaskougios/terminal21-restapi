package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.slf4j.LoggerFactory
import org.terminal21.client.ui.{UiElement, UiLib}
import org.terminal21.model.{CommandEvent, OnClick, Session}
import org.terminal21.ui.std.SessionsService

class ConnectedSession(val session: Session, sessionsService: SessionsService):
  private val logger                                           = LoggerFactory.getLogger(getClass)
  private var usingLibs                                        = List.empty[UiLib]
  def use[T <: UiLib](using factory: ConnectedSession => T): T =
    val lib = factory(this)
    synchronized:
      if usingLibs.contains(lib) then throw new IllegalStateException(s"Please use $lib only once.")
      usingLibs = lib :: usingLibs
    lib

  private var elements = List.empty[UiElement]

  def add(e: UiElement): Unit =
    synchronized:
      elements =
        if elements.exists(_.key == e.key) then
          elements.map: el =>
            if el.key == e.key then e else el
        else e :: elements
    send()

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

  private def send(): Unit =
    val j = toJson
    sessionsService.setSessionJsonState(session, j.toJson.noSpaces)

  private def toJson: JsonObject =
    val usingLibsCopy = synchronized(usingLibs)
    val elementsCopy  = synchronized(elements).reverse
    val json          = for
      e   <- elementsCopy
      lib <- usingLibsCopy
      j   <- lib.toJson(e)
    yield j
    JsonObject(("elements", json.asJson))
