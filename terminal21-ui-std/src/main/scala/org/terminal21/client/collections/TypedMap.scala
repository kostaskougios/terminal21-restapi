package org.terminal21.client.collections

class TypedMap(val m: Map[TypedMapKey[_], Any]):
  def +[A](kv: (TypedMapKey[A], A)): TypedMap        = new TypedMap(m + kv)
  def apply[A](k: TypedMapKey[A]): A                 = m(k).asInstanceOf[A]
  def getOrElse[A](k: TypedMapKey[A], default: => A) = m.getOrElse(k, default).asInstanceOf[A]
  def contains[A](k: TypedMapKey[A])                 = m.contains(k)

object TypedMap:
  def empty = new TypedMap(Map.empty)

trait TypedMapKey[A]
