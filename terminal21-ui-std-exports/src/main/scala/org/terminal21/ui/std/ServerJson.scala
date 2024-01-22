package org.terminal21.ui.std

import io.circe.Json

case class ServerJson(
    rootKeys: Seq[String],
    elements: Map[String, Json],
    keyTree: Map[String, Seq[String]]
):
  def include(j: ServerJson): ServerJson =
    ServerJson(
      rootKeys,
      elements ++ j.elements,
      keyTree ++ j.keyTree
    )

object ServerJson:
  val Empty = ServerJson(Nil, Map.empty, Map.empty)
