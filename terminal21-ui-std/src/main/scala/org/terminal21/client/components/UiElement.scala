package org.terminal21.client.components

import org.terminal21.client.collections.{TypedMap, TypedMapKey}
import org.terminal21.client.ConnectedSession

trait UiElement extends AnyElement:
  def key: String

  /** @return
    *   this element along all it's children flattened
    */
  def flat: Seq[UiElement] = Seq(this)

object UiElement:
  trait Current[A <: UiElement]:
    this: UiElement =>
    def current: A = ???

  trait HasChildren[A <: UiElement]:
    this: A =>
    def children: Seq[UiElement]
    override def flat: Seq[UiElement]  = Seq(this) ++ children.flatMap(_.flat)
    def withChildren(cn: UiElement*): A
    def noChildren: A                  = withChildren()
    def addChildren(cn: UiElement*): A = withChildren(children ++ cn: _*)

  trait HasEventHandler[A]:
    def defaultEventHandler: String => A

  trait HasStyle[A <: UiElement]:
    def style: Map[String, Any]
    def withStyle(v: Map[String, Any]): A
    def withStyle(vs: (String, Any)*): A = withStyle(vs.toMap)

  trait HasDataStore[A <: UiElement]:
    this: A =>
    def dataStore: TypedMap
    def withDataStore(ds: TypedMap): A
    def store[V](key: TypedMapKey[V], value: V): A = withDataStore(dataStore + (key -> value))
