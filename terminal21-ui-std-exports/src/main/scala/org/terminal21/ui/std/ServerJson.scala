package org.terminal21.ui.std

import io.circe.Json

case class ServerJson(
    rootKeys: Seq[String],
    elements: Map[String, Json],
    keyTree: Map[String, Seq[String]]
):
  private def allChildren(k: String): Seq[String] =
    val ch = keyTree(k)
    ch ++ ch.flatMap(allChildren)

  def include(j: ServerJson): ServerJson =
    val allCurrentChildren = j.rootKeys.flatMap(allChildren)
//    println(s"Removing     : ${allCurrentChildren.mkString(",")}")
//    println(s"j   Elements : ${j.elements.keys.toList.sorted.mkString(", ")}")
    val sj                 = ServerJson(
      rootKeys,
      (elements -- allCurrentChildren) ++ j.elements,
      (keyTree -- allCurrentChildren) ++ j.keyTree
    )
//    println(s"New Elements : ${sj.elements.keys.toList.sorted.mkString(", ")}")
    sj

object ServerJson:
  val Empty = ServerJson(Nil, Map.empty, Map.empty)
