package org.terminal21.sparklib.endtoend

import org.apache.spark.sql.{Dataset, SparkSession}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{*, given}
import org.terminal21.sparklib.SparkSessions
import org.terminal21.sparklib.endtoend.model.CodeFile
import org.terminal21.sparklib.endtoend.model.CodeFile.createDatasetFromProjectsSourceFiles
import org.terminal21.sparklib.steps.{SparkCalculation, StdSparkCalculation}

@main def sparkBasics(): Unit =
  SparkSessions.newTerminal21WithSparkSession(SparkSessions.newSparkSession(), "spark-basics", "Spark Basics"): (spark, session) =>
    given ConnectedSession = session
    given SparkSession     = spark

    import scala3encoders.given
    import spark.implicits.*

    val headers = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate")

    val sortedFilesTable = QuickTable.quickTable().withStringHeaders(headers: _*).build
    val sortedFilesBadge = Badge()
    val sortedCalc       = Calculation
      .newCalculation(sortedSourceFiles)
      .whenResultsNotReady:
        sortedFilesBadge.text = "Calculating..."
        session.render()
      .whenResultsReady: data =>
        sortedFilesBadge.text = "Ready"
        sortedFilesTable.withRowStringData(data.take(10).toList.map(_.toData))
        session.render()
      .build

    val codeFilesTable = QuickTable.quickTable().withStringHeaders(headers: _*).build

    object CodeFilesCalculation
        extends StdSparkCalculation[Unit, Dataset[CodeFile]](name = "Code files", dataUi = codeFilesTable, notifyWhenCalcReady = Seq(sortedCalc)):
      override protected def calculation(in: Unit)                              = createDatasetFromProjectsSourceFiles.toDS
      override protected def whenResultsReady(results: Dataset[CodeFile]): Unit =
        val dt = results.take(10).toList
        codeFilesTable.withRowStringData(dt.map(_.toData))
        super.whenResultsReady(results)

    Seq(
      CodeFilesCalculation,
      Box(text = "Code files sorted by date", bg = "green", p = 4),
      sortedFilesBadge,
      sortedFilesTable
    ).render()

    CodeFilesCalculation.run(())
    session.waitTillUserClosesSession()

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc)
