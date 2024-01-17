package org.terminal21.client.components

import org.terminal21.client.{ConnectedSession, EventHandler}

trait UiElement:
  def key: String
  def flat: Seq[UiElement] = Seq(this)

  def render()(using session: ConnectedSession): Unit =
    session.render(this)

  /** Renders any changes for this element and it's children (if any). The element must previously have been added to the session.
    */
  def renderChanges()(using session: ConnectedSession): Unit =
    session.renderChanges(this)

object UiElement:
  def allDeep(elements: Seq[UiElement]): Seq[UiElement] =
    elements ++ elements
      .collect:
        case hc: HasChildren[_] => allDeep(hc.children)
      .flatten

  trait HasChildren[A <: UiElement]:
    this: A =>
    def children: Seq[UiElement]

    override def flat: Seq[UiElement] = Seq(this) ++ children.flatMap(_.flat)

    def withChildren(cn: UiElement*): A

  trait HasEventHandler:
    def defaultEventHandler: EventHandler

  trait HasStyle[A <: UiElement]:
    def style: Map[String, Any]
    def style(v: Map[String, Any]): A
