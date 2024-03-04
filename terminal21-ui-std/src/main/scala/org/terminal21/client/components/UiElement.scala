package org.terminal21.client.components

import org.terminal21.client.Model
import org.terminal21.client.components.UiElement.{HasChildren, UiElementModelsKey}
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
  def onModelChangeRender[M](model: Model[M])(f: (This, M) => This): This =
    store(UiElementModelsKey, handledModels :+ model).store(model.OnModelChangeRenderKey, f.asInstanceOf[model.OnModelChangeFunction]).asInstanceOf[This]
  def hasModelChangeRenderHandler[M](model: Model[M]): Boolean            = dataStore.contains(model.OnModelChangeRenderKey)
  def fireModelChangeRender[M](model: Model[M])(m: M)                     =
    dataStore(model.OnModelChangeRenderKey).apply(this, m)
  def handledModels: Seq[Model[_]]                                        = dataStore.get(UiElementModelsKey).toSeq.flatten

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
  object UiElementModelsKey extends TypedMapKey[Seq[Model[_]]]

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
