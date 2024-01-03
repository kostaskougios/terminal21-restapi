package org.sparkonscala3.lib.util

import org.apache.commons.lang3.StringUtils

object Environment:
  val tmpDirectory =
    val t = System.getProperty("java.io.tmpdir")
    if (t.endsWith("/")) StringUtils.substringBeforeLast(t, "/") else t
