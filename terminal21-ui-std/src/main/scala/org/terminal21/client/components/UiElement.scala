package org.terminal21.client.components

import org.terminal21.collections.{TypedMap, TypedMapKey}

trait UiElement extends AnyElement:
  def key: String

  /** @return
    *   this element along all it's children flattened
    */
  def flat: Seq[UiElement] = Seq(this)

object UiElement:
  trait HasChildren[A <: UiElement]:
    this: A =>
    def children: Seq[UiElement]
    override def flat: Seq[UiElement]  = Seq(this) ++ children.flatMap(_.flat)
    def withChildren(cn: UiElement*): A
    def noChildren: A                  = withChildren()
    def addChildren(cn: UiElement*): A = withChildren(children ++ cn: _*)

  trait HasEventHandler:
    type This <: UiElement
    def defaultEventHandler: String => This

  trait HasStyle[A <: UiElement]:
    def style: Map[String, Any]
    def withStyle(v: Map[String, Any]): A
    def withStyle(vs: (String, Any)*): A = withStyle(vs.toMap)

  trait HasDataStore:
    this: UiElement =>
    type This <: UiElement
    def dataStore: TypedMap
    def withDataStore(ds: TypedMap): This
    def store[V](key: TypedMapKey[V], value: V): This = withDataStore(dataStore + (key -> value))
