package org.terminal21.sparklib.endtoend

import org.apache.spark.sql.{Dataset, SparkSession}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{*, given}
import org.terminal21.sparklib.*
import org.terminal21.sparklib.endtoend.model.CodeFile
import org.terminal21.sparklib.endtoend.model.CodeFile.scanSourceFiles

@main def sparkBasics(): Unit =
  SparkSessions.newTerminal21WithSparkSession(SparkSessions.newSparkSession(), "spark-basics", "Spark Basics"): (spark, session) =>
    given ConnectedSession = session
    given SparkSession     = spark
    import scala3encoders.given
    import spark.implicits.*

    val headers = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate", "timestamp")

    val sortedFilesTable = QuickTable().headers(headers: _*).caption("Files sorted by createdDate and numOfWords")
    val codeFilesTable   = QuickTable().headers(headers: _*).caption("Unsorted files")

    val sortedCalc = sortedSourceFiles(sourceFiles()).visualize("Sorted files", sortedFilesTable): results =>
      val tableRows = results.take(3).toList.map(_.toData)
      sortedFilesTable.rows(tableRows)

    val codeFilesCalculation = sourceFiles().visualize("Code files", codeFilesTable): results =>
      val dt = results.take(3).toList
      codeFilesTable.rows(dt.map(_.toData))

    val sortedFilesTableDF = QuickTable().headers(headers: _*).caption("Files sorted by createdDate and numOfWords ASC and as DF")
    val sortedCalcDF       = sourceFiles()
      .sort($"createdDate".asc, $"numOfWords".asc)
      .toDF()
      .visualize("Sorted files DF", sortedFilesTableDF): results =>
        val tableRows = results.take(4).toList
        sortedFilesTableDF.rows(tableRows.toUiTable)

    Seq(
      codeFilesCalculation,
      sortedCalc,
      sortedCalcDF
    ).render()

    session.waitTillUserClosesSession()

def sourceFiles()(using spark: SparkSession) =
  import scala3encoders.given
  import spark.implicits.*
  scanSourceFiles.toDS.map: cf =>
    cf.copy(timestamp = System.currentTimeMillis())

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc, $"numOfWords".desc)
