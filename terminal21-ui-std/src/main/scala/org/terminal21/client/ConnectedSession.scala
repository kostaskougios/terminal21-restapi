package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.slf4j.LoggerFactory
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.{HasEventHandler, allDeep}
import org.terminal21.client.components.UiElementEncoding
import org.terminal21.model.*
import org.terminal21.ui.std.SessionsService

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}
import scala.annotation.tailrec

class ConnectedSession(val session: Session, encoding: UiElementEncoding, val serverUrl: String, sessionsService: SessionsService, onCloseHandler: () => Unit):
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

  private val exitLatch = new CountDownLatch(1)

  /** Waits till user closes the session by clicking the session close [X] button.
    */
  def waitTillUserClosesSession(): Unit =
    try exitLatch.await()
    catch case _: Throwable => () // nop

  private val leaveSessionOpen = new AtomicBoolean(false)

  /** Doesn't close the session upon exiting. In the UI the session seems active but events are not working because the event handlers are not available.
    */
  def leaveSessionOpenAfterExiting(): Unit =
    leaveSessionOpen.set(true)

  def isLeaveSessionOpen: Boolean = leaveSessionOpen.get()

  /** Waits till user closes the session or a custom condition becomes true
    * @param condition
    *   if true then this returns otherwise it waits.
    */
  @tailrec final def waitTillUserClosesSessionOr(condition: => Boolean): Unit =
    exitLatch.await(100, TimeUnit.MILLISECONDS)
    if exitLatch.getCount == 0 || condition then () else waitTillUserClosesSessionOr(condition)

  /** @return
    *   true if user closed the session via the close button
    */
  def isClosed: Boolean = exitLatch.getCount == 0

  def click(e: UiElement): Unit = fireEvent(OnClick(e.key))

  private[client] def fireEvent(event: CommandEvent): Unit =
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

  def allElements: Seq[UiElement] = synchronized(elements)

  private def toJson: JsonObject =
    import encoding.given
    val elementsCopy = allElements
    val json         =
      for e <- elementsCopy
      yield e.asJson.deepDropNullValues
    JsonObject(("elements", json.asJson))
