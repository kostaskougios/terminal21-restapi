package org.terminal21.ui.std

import io.circe.Json

case class ServerJson(
    rootKeys: Seq[String],
    elements: Map[String, Json],
    keyTree: Map[String, Seq[String]]
)

object ServerJson:
  val Empty = ServerJson(Nil, Map.empty, Map.empty)
