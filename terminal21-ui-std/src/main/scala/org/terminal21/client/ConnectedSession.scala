package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.slf4j.LoggerFactory
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.{HasEventHandler, allDeep}
import org.terminal21.client.components.UiElementEncoding.uiElementEncoder
import org.terminal21.model.*
import org.terminal21.ui.std.SessionsService

import java.util.concurrent.CountDownLatch

class ConnectedSession(val session: Session, val serverUrl: String, sessionsService: SessionsService, onCloseHandler: () => Unit):
  private val logger   = LoggerFactory.getLogger(getClass)
  private var elements = List.empty[UiElement]

  def uiUrl: String = serverUrl + "/ui"
  def clear(): Unit = synchronized:
    elements = Nil

  def add(es: UiElement*): Unit =
    val withEvents = allDeep(es).collect:
      case h: HasEventHandler => h

    for e <- withEvents do addEventHandlerAtTheTop(e.key, e.defaultEventHandler)

    synchronized:
      elements = elements ::: es.toList

  private val eventHandlers = collection.concurrent.TrieMap.empty[String, List[EventHandler]]

  private def addEventHandlerAtTheTop(key: String, handler: EventHandler): Unit =
    val handlers = eventHandlers.getOrElse(key, Nil)
    eventHandlers += key -> (handler :: handlers)

  def addEventHandler(key: String, handler: EventHandler): Unit =
    val handlers = eventHandlers.getOrElse(key, Nil)
    eventHandlers += key -> (handlers :+ handler)

  private val exitLatch                 = new CountDownLatch(1)
  def waitTillUserClosesSession(): Unit =
    try exitLatch.await()
    catch case _: Throwable => () // nop

  def fireEvent(event: CommandEvent): Unit =
    event match
      case SessionClosed(_) =>
        exitLatch.countDown()
        onCloseHandler()
      case _                =>
        eventHandlers.get(event.key) match
          case Some(handlers) =>
            for handler <- handlers do
              (event, handler) match
                case (_: OnClick, h: OnClickEventHandler)                 => h.onClick()
                case (onChange: OnChange, h: OnChangeEventHandler)        => h.onChange(onChange.value)
                case (onChange: OnChange, h: OnChangeBooleanEventHandler) => h.onChange(onChange.value.toBoolean)
                case x                                                    => logger.error(s"Unknown event handling combination : $x")
          case None           =>
            logger.warn(s"There is no event handler for event $event")

  def render(): Unit =
    val j = toJson
    sessionsService.setSessionJsonState(session, j.toJson.noSpaces)

  private def toJson: JsonObject =
    val elementsCopy = synchronized(elements)
    val json         =
      for e <- elementsCopy
      yield e.asJson.deepDropNullValues
    JsonObject(("elements", json.asJson))
