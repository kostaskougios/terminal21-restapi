package org.terminal21.client

import io.circe.*
import io.circe.generic.auto.*
import org.slf4j.LoggerFactory
import org.terminal21.client.components.UiElement.HasChildren
import org.terminal21.client.components.{UiComponent, UiElement, UiElementEncoding}
import org.terminal21.client.internal.EventHandlers
import org.terminal21.client.model.{GlobalEvent, SessionClosedEvent, UiEvent}
import org.terminal21.collections.SEList
import org.terminal21.model.*
import org.terminal21.ui.std.{ServerJson, SessionsService}

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}
import scala.annotation.tailrec
import scala.collection.concurrent.TrieMap

class ConnectedSession(val session: Session, encoding: UiElementEncoding, val serverUrl: String, sessionsService: SessionsService, onCloseHandler: () => Unit):
  private val logger           = LoggerFactory.getLogger(getClass)
  private val handlers         = new EventHandlers(this)
  @volatile private var events = SEList[GlobalEvent]()

  def uiUrl: String = serverUrl + "/ui"

  /** Clears all UI elements and event handlers. Renders a blank UI
    */
  def clear(): Unit =
    render()
    handlers.clear()
    modifiedElements.clear()
    removeGlobalEventHandler()
    events.poisonPill()
    events = SEList()

  def addEventHandler(key: String, handler: EventHandler): Unit = handlers.addEventHandler(key, handler)

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

  @volatile private var globalEventHandler: Option[GlobalEventHandler] = None

  /** Registers a global event handler who will handle all received events.
    *
    * @param h
    *   GlobalEventHandler
    */
  def withGlobalEventHandler(h: GlobalEventHandler): Unit =
    globalEventHandler = Some(h)

  def eventIterator: Iterator[GlobalEvent] = events.iterator

  /** removes the global event handler (if any). No more events will be received by that handler.
    */
  def removeGlobalEventHandler(): Unit = globalEventHandler = None

  def fireEvent(event: CommandEvent): Unit =
    try
      event match
        case SessionClosed(_) =>
          events.add(SessionClosedEvent)
          events.poisonPill()
          exitLatch.countDown()
          onCloseHandler()
        case _                =>
          handlers.getEventHandler(event.key) match
            case Some(handlers) =>
              for handler <- handlers do
                (event, handler) match
                  case (_: OnClick, h: OnClickEventHandler)                 => h.onClick()
                  case (onChange: OnChange, h: OnChangeEventHandler)        => h.onChange(onChange.value)
                  case (onChange: OnChange, h: OnChangeBooleanEventHandler) => h.onChange(onChange.value.toBoolean)
                  case x                                                    => logger.error(s"Unknown event handling combination : $x")
            case None           => // nop
          val globalEvent = UiEvent(event, modifiedElements(event.key))
          for h <- globalEventHandler do h.onEvent(globalEvent)
          events.add(globalEvent)
    catch
      case t: Throwable =>
        logger.error(s"Session ${session.id}: An error occurred while handling $event", t)
        throw t

  def render(es: UiElement*): Unit =
    for e <- es.flatMap(_.flat) do modified(e)
    handlers.registerEventHandlers(es)
    val j = toJson(es)
    sessionsService.setSessionJsonState(session, j)

  def renderChanges(es: UiElement*): Unit =
    for e <- es.flatMap(_.flat) do modified(e)
    val j = toJson(es)
    sessionsService.changeSessionJsonState(session, j)

  private def toJson(elements: Seq[UiElement]): ServerJson =
    val flat = elements.flatMap(_.flat)
    val sj   = ServerJson(
      elements.map(_.key),
      flat
        .map: el =>
          (
            el.key,
            el match
              case e: UiComponent    => encoding.uiElementEncoder(e).deepDropNullValues
              case e: HasChildren[_] => encoding.uiElementEncoder(e.noChildren).deepDropNullValues
              case e                 => encoding.uiElementEncoder(e).deepDropNullValues
          )
        .toMap,
      flat
        .map: e =>
          (
            e.key,
            e match
              case e: UiComponent    => e.rendered.map(_.key)
              case e: HasChildren[_] => e.children.map(_.key)
              case _                 => Nil
          )
        .toMap
    )
    sj
  private val modifiedElements                             = TrieMap.empty[String, UiElement]
  def modified(e: UiElement): Unit                         =
    modifiedElements += e.key -> e
  def currentState[A <: UiElement](e: A): A =
    modifiedElements.getOrElse(e.key, throw new IllegalStateException(s"Key ${e.key} doesn't exist or was removed")).asInstanceOf[A]
