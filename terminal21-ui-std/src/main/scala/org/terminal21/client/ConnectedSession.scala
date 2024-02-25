package org.terminal21.client

import org.terminal21.client.components.UiElement.HasChildren
import org.terminal21.client.components.{UiComponent, UiElement}
import org.terminal21.client.json.UiElementEncoding
import org.terminal21.collections.SEList
import org.terminal21.model.*
import org.terminal21.ui.std.{ServerJson, SessionsService}

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}
import scala.annotation.tailrec

class ConnectedSession(val session: Session, encoding: UiElementEncoding, val serverUrl: String, sessionsService: SessionsService, onCloseHandler: () => Unit):
  @volatile private var events = SEList[CommandEvent]()

  def uiUrl: String = serverUrl + "/ui"

  /** Clears all UI elements and event handlers. Renders a blank UI
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

  def render(es: Seq[UiElement]): Unit =
    val j = toJson(es)
    sessionsService.setSessionJsonState(session, j)

  def renderChanges(es: Seq[UiElement]): Unit =
    if !isClosed && es.nonEmpty then
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
              case e: UiComponent => encoding.uiElementEncoder(e).deepDropNullValues
              case e: HasChildren => encoding.uiElementEncoder(e.noChildren).deepDropNullValues
              case e              => encoding.uiElementEncoder(e).deepDropNullValues
          )
        .toMap,
      flat
        .map: e =>
          (
            e.key,
            e match
              case e: UiComponent => e.rendered.map(_.key)
              case e: HasChildren => e.children.map(_.key)
              case _              => Nil
          )
        .toMap
    )
    sj
