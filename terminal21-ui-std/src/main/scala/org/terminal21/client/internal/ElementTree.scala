package org.terminal21.client.internal

import org.terminal21.client.EventHandler
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.UiElement.{HasEventHandler, allDeep}

class ElementTree:
  private var elements      = List.empty[UiElement]
  private var containedKeys = Set.empty[String]
  private val eventHandlers = collection.concurrent.TrieMap.empty[String, List[EventHandler]]

  def allElements: Seq[UiElement] = synchronized(elements)

  def containsKey(key: String): Boolean = containedKeys.contains(key)

  def add(es: Seq[UiElement]): Unit = synchronized:
    for e <- es do if containsKey(e.key) then throw new IllegalArgumentException(s"Key ${e.key} already added. Component: $e")
    val all        = allDeep(es)
    containedKeys = containedKeys ++ all.map(_.key)
    val withEvents = all.collect:
      case h: HasEventHandler => h

    for e <- withEvents do addEventHandlerAtTheTop(e.key, e.defaultEventHandler)
    elements = elements ::: es.toList

  def addEventHandler(key: String, handler: EventHandler): Unit =
    val handlers = eventHandlers.getOrElse(key, Nil)
    eventHandlers += key -> (handlers :+ handler)

  def getEventHandler(key: String): Option[List[EventHandler]] = eventHandlers.get(key)

  private def addEventHandlerAtTheTop(key: String, handler: EventHandler): Unit =
    val handlers = eventHandlers.getOrElse(key, Nil)
    eventHandlers += key -> (handler :: handlers)

  def clear(): Unit = synchronized:
    elements = Nil
    eventHandlers.clear()
    containedKeys = Set.empty
