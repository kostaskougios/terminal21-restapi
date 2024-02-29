package org.terminal21.collections

class TypedMap(val m: Map[TypedMapKey[_], Any]):
  def +[A](kv: (TypedMapKey[A], A)): TypedMap        = new TypedMap(m + kv)
  def apply[A](k: TypedMapKey[A]): A                 = m(k).asInstanceOf[A]
  def get[A](k: TypedMapKey[A]): Option[A]           = m.get(k).asInstanceOf[Option[A]]
  def getOrElse[A](k: TypedMapKey[A], default: => A) = m.getOrElse(k, default).asInstanceOf[A]

  override def hashCode()       = m.hashCode()
  override def equals(obj: Any) = obj match
    case tm: TypedMap => m == tm.m
    case _            => false

  def contains[A](k: TypedMapKey[A]) = m.contains(k)
  override def toString              = s"TypedMap(${m.keys.mkString(", ")})"

object TypedMap:
  def empty = new TypedMap(Map.empty)

trait TypedMapKey[A]
