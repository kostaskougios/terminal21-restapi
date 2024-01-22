package org.terminal21.ui.std

import io.circe.Json

object StdExportsBuilders:
  def serverJson(
      rootKeys: Seq[String] = Nil,
      elements: Map[String, Json] = Map.empty,
      keyTree: Map[String, Seq[String]] = Map.empty
  ) = ServerJson(
    rootKeys,
    elements,
    keyTree
  )
