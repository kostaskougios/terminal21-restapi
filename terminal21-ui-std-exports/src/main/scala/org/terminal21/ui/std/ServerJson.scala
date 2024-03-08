package org.terminal21.ui.std

import io.circe.Json
import org.slf4j.LoggerFactory

case class ServerJson(
    elements: Seq[Json]
)

object ServerJson:
  val Empty = ServerJson(Nil)
