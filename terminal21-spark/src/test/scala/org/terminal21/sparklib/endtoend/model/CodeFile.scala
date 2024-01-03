package org.terminal21.sparklib.endtoend.model

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.file.Files
import java.time.{Instant, LocalDate, ZoneId}

case class CodeFile(id: Int, name: String, path: String, numOfLines: Int, numOfWords: Int, createdDate: LocalDate):
  def toColumnNames: Seq[String] = productElementNames.toList
  def toData: Seq[String]        = productIterator.map(_.toString).toList

object CodeFile:
  import scala.jdk.CollectionConverters.*
  def createDatasetFromProjectsSourceFiles: Seq[CodeFile] =
    val availableFiles = FileUtils.listFiles(new File("."), Array("scala"), true).asScala.toArray
    for i <- 1 to 10000 yield
      val f    = availableFiles(i % availableFiles.length)
      val code = Files.readString(f.toPath)
      CodeFile(
        i,
        f.getName,
        f.getPath,
        code.split("\n").size,
        code.split(" ").size,
        LocalDate.ofInstant(Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault())
      )
