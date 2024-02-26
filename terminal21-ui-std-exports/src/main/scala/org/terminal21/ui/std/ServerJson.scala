package org.terminal21.ui.std

import io.circe.Json
import org.slf4j.LoggerFactory

case class ServerJson(
    rootKeys: Seq[String],
    elements: Map[String, Json],
    keyTree: Map[String, Seq[String]]
):
  private def allChildren(k: String): Seq[String] =
    val ch = keyTree(k)
    ch ++ ch.flatMap(allChildren)

  def include(j: ServerJson): ServerJson =
    try
      val allCurrentChildren = j.rootKeys.flatMap(allChildren)
      val sj                 = ServerJson(
        rootKeys,
        (elements -- allCurrentChildren) ++ j.elements,
        (keyTree -- allCurrentChildren) ++ j.keyTree
      )
      sj
    catch
      case t: Throwable =>
        LoggerFactory
          .getLogger(getClass)
          .error(
            s"""
             |Got an invalid ServerJson that caused an error.
             |Before receiving:
             |${toHumanReadableString}
             |The received:
             |${j.toHumanReadableString}
             |""".stripMargin
          )
        throw t

  def toHumanReadableString: String =
    s"""
       |Root keys    : ${rootKeys.mkString(", ")}
       |Element keys : ${elements.keys.mkString(", ")}
       |keyTree      : ${keyTree.mkString(", ")}
       |""".stripMargin
object ServerJson:
  val Empty = ServerJson(Nil, Map.empty, Map.empty)
