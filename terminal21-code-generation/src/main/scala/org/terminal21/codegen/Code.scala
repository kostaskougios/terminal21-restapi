package org.terminal21.codegen

import org.apache.commons.io.FileUtils

import java.io.File

case class Code(file: String, code: String):
  def writeTo(srcRootFolder: String): Unit =
    val f = new File(srcRootFolder, file)
    FileUtils.writeStringToFile(f, code, "UTF-8")
