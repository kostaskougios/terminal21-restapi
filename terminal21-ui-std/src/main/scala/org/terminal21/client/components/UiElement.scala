package org.terminal21.client.components

import org.terminal21.client.Model
import org.terminal21.client.components.UiElement.HasChildren
import org.terminal21.client.components.chakra.Box
import org.terminal21.collections.{TypedMap, TypedMapKey}

abstract class UiElement extends AnyElement:
  type This <: UiElement

  def key: String
  def withKey(key: String): This
  def findKey(key: String): UiElement = flat.find(_.key == key).get

  def dataStore: TypedMap
  def withDataStore(ds: TypedMap): This
  def store[V](key: TypedMapKey[V], value: V): This = withDataStore(dataStore + (key -> value))

  /** This handler will be called whenever the model changes. It will also be called with the initial model before the first render()
    */
  def onModelChange[M](using model: Model[M])(f: (This, M) => This): This =
    store(model.OnModelChangeKey, f.asInstanceOf[model.OnModelChangeFunction])
  def hasModelChangeHandler[M](using model: Model[M]): Boolean            = dataStore.contains(model.OnModelChangeKey)
  def fireModelChange[M](using model: Model[M])(m: M)                     =
    dataStore(model.OnModelChangeKey).apply(this, m)

  /** @return
    *   this element along all it's children flattened
    */
  def flat: Seq[UiElement] = Seq(this)

  def substituteComponents: UiElement =
    this match
      case c: UiComponent  => Box(key = c.key, text = "", children = c.rendered.map(_.substituteComponents), dataStore = c.dataStore)
      case ch: HasChildren => ch.withChildren(ch.children.map(_.substituteComponents)*)
      case _               => this

  def toSimpleString: String = s"${getClass.getSimpleName}($key)"

object UiElement:
  trait HasChildren:
    this: UiElement =>
    def children: Seq[UiElement]
    override def flat: Seq[UiElement]     = Seq(this) ++ children.flatMap(_.flat)
    def withChildren(cn: UiElement*): This
    def noChildren: This                  = withChildren()
    def addChildren(cn: UiElement*): This = withChildren(children ++ cn: _*)

  trait HasStyle:
    this: UiElement =>
    def style: Map[String, Any]
    def withStyle(v: Map[String, Any]): This
    def withStyle(vs: (String, Any)*): This = withStyle(vs.toMap)
