package org.terminal21.sparklib.endtoend

import org.apache.spark.sql.{Dataset, SparkSession}
import org.terminal21.client.*
import org.terminal21.client.given
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.sparklib.SparkSessions
import org.terminal21.sparklib.endtoend.model.CodeFile
import org.terminal21.sparklib.endtoend.model.CodeFile.createDatasetFromProjectsSourceFiles
import org.terminal21.sparklib.steps.Steps

import java.time.LocalDate

@main def sparkBasics(): Unit =
  SparkSessions.newTerminal21WithSparkSession(SparkSessions.newSparkSession(), "spark-basics", "Spark Basics"): (spark, session) =>
    given ConnectedSession = session
    given SparkSession     = spark

    val headers          = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate")
    val codeFilesTable   = QuickTable.quickTable().withStringHeaders(headers: _*).build
    val codeFilesBadge   = Badge()
    val sortedFilesTable = QuickTable.quickTable().withStringHeaders(headers: _*).build
    val sortedFilesBadge = Badge()

    Seq(
      Box(text = "Code files", bg = "green", p = 4),
      codeFilesBadge,
      codeFilesTable,
      Box(text = "Code files sorted by date", bg = "green", p = 4),
      sortedFilesBadge,
      sortedFilesTable
    ).render()

    val sortedCalc       = Calculation
      .newCalculation(sortedSourceFiles)
      .whenStartingCalculationUpdateUi:
        sortedFilesBadge.text = "Calculating..."
        session.render()
      .whenCalculatedUpdateUi: data =>
        sortedFilesBadge.text = "Ready"
        sortedFilesTable.withRowStringData(data.take(10).toList.map(_.toData))
        session.render()
      .build
    val calcSrcCodeFiles = calculateSourceCodeFiles(codeFilesTable, codeFilesBadge)
    calcSrcCodeFiles.notifyAfterCalculated(sortedCalc).build.apply(())

    session.waitTillUserClosesSession()

def calculateSourceCodeFiles(table: TableContainer, badge: Badge)(using spark: SparkSession, session: ConnectedSession) =
  import spark.implicits.*
  import scala3encoders.given
  Calculation
    .newCalculationNoIn:
      createDatasetFromProjectsSourceFiles.toDS
    .whenStartingCalculationUpdateUi:
      table.withRowStringData(Nil)
      badge.text = "Calculating..."
      session.render()
    .whenCalculatedUpdateUi: tableData =>
      table.withRowStringData(tableData.take(10).toList.map(_.toData))
      badge.text = "Ready"
      session.render()

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc)
