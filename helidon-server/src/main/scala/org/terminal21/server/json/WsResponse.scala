package org.terminal21.server.json

import io.circe.*
import io.circe.generic.auto.*
import org.terminal21.ui.std.json.StdElement

sealed trait WsResponse

// std react/html components
case class Std(elements: Seq[StdElement]) extends WsResponse
