package org.terminal21.ui.std

import io.circe.Json

object StdExportsBuilders:
  def serverJson(
      elements: Seq[Json] = Nil
  ) = ServerJson(elements)
