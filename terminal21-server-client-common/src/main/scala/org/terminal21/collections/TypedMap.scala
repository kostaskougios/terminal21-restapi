package org.terminal21.collections

import scala.reflect.{ClassTag, classTag}

type TMMap = Map[TypedMapKey[_], Any]

class TypedMap(protected val m: TMMap):
  def +[A](kv: (TypedMapKey[A], A)): TypedMap        = new TypedMap(m + kv)
  def apply[A](k: TypedMapKey[A]): A                 = m(k).asInstanceOf[A]
  def get[A](k: TypedMapKey[A]): Option[A]           = m.get(k).asInstanceOf[Option[A]]
  def getOrElse[A](k: TypedMapKey[A], default: => A) = m.getOrElse(k, default).asInstanceOf[A]
  def keys: Iterable[TypedMapKey[_]]                 = m.keys

  override def hashCode()       = m.hashCode()
  override def equals(obj: Any) = obj match
    case tm: TypedMap => m == tm.m
    case _            => false

  def contains[A](k: TypedMapKey[A]) = m.contains(k)
  override def toString              = s"TypedMap(${m.keys.mkString(", ")})"

object TypedMap:
  def empty                             = new TypedMap(Map.empty)
  def apply(kv: (TypedMapKey[_], Any)*) =
    val m = Map(kv*)
    new TypedMap(m)

trait TypedMapKey[A: ClassTag]:
  type Of = A

  override def toString = s"${getClass.getSimpleName}[${classTag[A].runtimeClass.getSimpleName}]"
