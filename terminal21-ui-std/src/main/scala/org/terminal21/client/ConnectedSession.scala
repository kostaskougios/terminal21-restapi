package org.terminal21.client

import org.terminal21.client.components.UiElement.HasChildren
import org.terminal21.client.components.chakra.Box
import org.terminal21.client.components.{UiComponent, UiElement}
import org.terminal21.client.json.UiElementEncoding
import org.terminal21.collections.SEList
import org.terminal21.model.*
import org.terminal21.ui.std.{ServerJson, SessionsService}

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}
import scala.annotation.tailrec

/** A session connected to the terminal21 server.
  *
  * @param session
  *   the session
  * @param encoding
  *   json encoder for UiElements
  * @param serverUrl
  *   the url of the server
  * @param sessionsService
  *   the service to talk to the server
  * @param onCloseHandler
  *   gets notified when the user closes the session
  */
class ConnectedSession(val session: Session, encoding: UiElementEncoding, val serverUrl: String, sessionsService: SessionsService, onCloseHandler: () => Unit):
  @volatile private var events = SEList[CommandEvent]()

  def uiUrl: String = serverUrl + "/ui"

  /** Clears all UI elements and event handlers.
    */
  def clear(): Unit =
    events.poisonPill()
    events = SEList()

  private val exitLatch = new CountDownLatch(1)

  /** Waits till user closes the session by clicking the session close [X] button.
    */
  def waitTillUserClosesSession(): Unit =
    try exitLatch.await()
    catch case _: Throwable => () // nop

  private val leaveSessionOpen = new AtomicBoolean(false)

  /** Doesn't close the session upon exiting. In the UI the session seems active but events are not working because the event handlers are not available. Useful
    * when we need to let the user read through some data. But no interaction is possible anymore between the user and the code.
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

  def fireClickEvent(e: UiElement): Unit                    = fireEvent(CommandEvent.onClick(e))
  def fireChangeEvent(e: UiElement, newValue: String): Unit = fireEvent(CommandEvent.onChange(e, newValue))
  def fireSessionClosedEvent(): Unit                        = fireEvent(CommandEvent.sessionClosed)

  def eventIterator: Iterator[CommandEvent] = events.iterator

  /** Waits until at least 1 event iterator was created for the current page. Useful for testing purposes if i.e. one thread runs the main loop and gets an
    * eventIterator at some point and an other thread needs to fire events.
    */
  def waitUntilAtLeast1EventIteratorWasCreated(): Unit = events.waitUntilAtLeast1IteratorWasCreated()

  def fireEvents(events: CommandEvent*): Unit = for e <- events do fireEvent(e)

  def fireEvent(event: CommandEvent): Unit =
    events.add(event)
    event match
      case SessionClosed(_) =>
        events.poisonPill()
        exitLatch.countDown()
        onCloseHandler()
      case _                =>

  /** Normally this method shouldn't be called directly. Terminates any previous event iterators, clears the UI and renders the UiElements.
    * @param es
    *   the UiElements to be rendered.
    */
  def render(es: Seq[UiElement]): Unit =
    clear()
    val j = toJson(es)
    sessionsService.setSessionJsonState(session, j)

  /** Normally this method shouldn't be called directly. Renders updates to existing elements
    * @param es
    *   a seq of updated elements, all these should already have been rendered before (but not necessarily their children)
    */
  private[client] def renderChanges(es: Seq[UiElement]): Unit =
    if !isClosed && es.nonEmpty then
      val j = toJson(es)
      sessionsService.setSessionJsonState(session, j) // TODO:changeSessionJsonState

  private def toJson(elementsUn: Seq[UiElement]): ServerJson =
    val elements = elementsUn.map(_.substituteComponents)
    val sj       = ServerJson(
      elements.map(e => encoding.uiElementEncoder(e).deepDropNullValues)
    )
    sj
