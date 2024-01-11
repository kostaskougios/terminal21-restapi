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
        case hc: HasChildren[_] => allDeep(hc.children)
      .flatten

  trait HasChildren[A]:
    this: A =>
    var children: Seq[UiElement]

    def withChildren(cn: UiElement*): A =
      children = cn
      this

    def addChildren(e: UiElement*): A =
      children = children ++ e
      this

  trait HasEventHandler:
    def defaultEventHandler: EventHandler

  trait HasStyle:
    var style: Map[String, Any]
