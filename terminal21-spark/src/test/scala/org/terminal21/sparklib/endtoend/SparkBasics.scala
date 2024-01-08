package org.terminal21.sparklib.endtoend

import org.apache.spark.sql.{Dataset, SparkSession}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{*, given}
import org.terminal21.sparklib.SparkSessions
import org.terminal21.sparklib.endtoend.model.CodeFile
import org.terminal21.sparklib.endtoend.model.CodeFile.createDatasetFromProjectsSourceFiles
import org.terminal21.sparklib.calculations.SparkCalculation.sparkCalculation
import org.terminal21.sparklib.calculations.{SparkCalculation, StdUiSparkCalculation}

@main def sparkBasics(): Unit =
  SparkSessions.newTerminal21WithSparkSession(SparkSessions.newSparkSession(), "spark-basics", "Spark Basics"): (spark, session) =>
    given ConnectedSession = session
    given SparkSession     = spark

    import scala3encoders.given
    import spark.implicits.*

    val headers = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate")

    val sortedFilesTable = QuickTable().headers(headers: _*).caption("Files sorted by createdDate and numOfWords")
    val sortedCalc       = sparkCalculation("Sorted files", sortedFilesTable)(sortedSourceFiles)
      .whenResultsReady: results =>
        val tableRows = results.take(10).toList.map(_.toData)
        sortedFilesTable.rows(tableRows)

    val codeFilesTable       = QuickTable().headers(headers: _*).caption("Unsorted files")
    val codeFilesCalculation = sparkCalculation("Code files", codeFilesTable, sortedCalc): _ =>
      createDatasetFromProjectsSourceFiles.toDS
    .whenResultsReady: results =>
      val dt = results.take(10).toList
      codeFilesTable.rows(dt.map(_.toData))

    Seq(
      codeFilesCalculation,
      sortedCalc
    ).render()

    codeFilesCalculation.run(())
    session.waitTillUserClosesSession()

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc, $"numOfWords".desc)
