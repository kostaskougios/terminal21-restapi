package org.terminal21.client.components

import org.terminal21.client.{ConnectedSession, EventHandler}

trait UiElement:
  def key: String

  def render()(using session: ConnectedSession): Unit =
    session.add(this)
    session.render()

object UiElement:
  def allDeep(elements: Seq[UiElement]): Seq[UiElement] =
    elements ++ elements
      .collect:
        case hc: HasChildren => allDeep(hc.children)
      .flatten

  trait HasChildren:
    def children: Seq[UiElement]

  trait HasEventHandler:
    def defaultEventHandler: EventHandler
