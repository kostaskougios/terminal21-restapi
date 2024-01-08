package org.terminal21.sparklib.endtoend.model

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.file.Files
import java.time.{Instant, LocalDate, ZoneId}

case class CodeFile(id: Int, name: String, path: String, numOfLines: Int, numOfWords: Int, createdDate: LocalDate, timestamp: Long):
  def toColumnNames: Seq[String] = productElementNames.toList
  def toData: Seq[String]        = productIterator.map(_.toString).toList

object CodeFile:
  import scala.jdk.CollectionConverters.*
  def scanSourceFiles: Seq[CodeFile] =
    val availableFiles = FileUtils.listFiles(new File(".."), Array("scala"), true).asScala.toList
    availableFiles.zipWithIndex.map: (f, i) =>
      val code = Files.readString(f.toPath)
      CodeFile(
        i,
        f.getName,
        f.getPath,
        code.split("\n").length,
        code.split(" ").length,
        LocalDate.ofInstant(Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault()),
        System.currentTimeMillis()
      )
