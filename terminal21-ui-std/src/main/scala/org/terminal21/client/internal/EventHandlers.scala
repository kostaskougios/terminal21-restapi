package org.terminal21.client.internal

import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.HasEventHandler
import org.terminal21.client.{ConnectedSession, EventHandler}

class EventHandlers(session: ConnectedSession):
  private val eventHandlers = collection.concurrent.TrieMap.empty[String, List[EventHandler]]

  def registerEventHandlers(es: Seq[UiElement]): Unit = synchronized:
    val all        = es.flatMap(_.flat)
    val withEvents = all.collect:
      case h: HasEventHandler => h

    for e <- withEvents do addEventHandlerAtTheTop(e.key, e.defaultEventHandler(session))

  def addEventHandler(key: String, handler: EventHandler): Unit =
    val handlers = eventHandlers.getOrElse(key, Nil)
    eventHandlers += key -> (handlers :+ handler)

  def getEventHandler(key: String): Option[List[EventHandler]] = eventHandlers.get(key)

  private def addEventHandlerAtTheTop(key: String, handler: EventHandler): Unit =
    val handlers = eventHandlers.getOrElse(key, Nil)
    eventHandlers += key -> (handler :: handlers)

  def clear(): Unit = synchronized:
    eventHandlers.clear()
