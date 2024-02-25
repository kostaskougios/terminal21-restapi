package org.terminal21.client.components

import org.terminal21.collections.{TypedMap, TypedMapKey}

trait UiElement extends AnyElement:
  type This <: UiElement

  def key: String

  /** @return
    *   this element along all it's children flattened
    */
  def flat: Seq[UiElement] = Seq(this)

object UiElement:
  trait HasChildren:
    this: UiElement =>
    def children: Seq[UiElement]
    override def flat: Seq[UiElement]     = Seq(this) ++ children.flatMap(_.flat)
    def withChildren(cn: UiElement*): This
    def noChildren: This                  = withChildren()
    def addChildren(cn: UiElement*): This = withChildren(children ++ cn: _*)

  trait HasEventHandler:
    this: UiElement =>
    def defaultEventHandler: String => This

  trait HasStyle:
    this: UiElement =>
    def style: Map[String, Any]
    def withStyle(v: Map[String, Any]): This
    def withStyle(vs: (String, Any)*): This = withStyle(vs.toMap)

  trait HasDataStore:
    this: UiElement =>
    def dataStore: TypedMap
    def withDataStore(ds: TypedMap): This
    def store[V](key: TypedMapKey[V], value: V): This = withDataStore(dataStore + (key -> value))
