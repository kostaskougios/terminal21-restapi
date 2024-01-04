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

    val codeFilesTable = QuickTable.quickTable().withStringHeaders("id", "name", "path", "numOfLines", "numOfWords", "createdDate").build
    val codeFilesBadge = Badge()

    Seq(
      codeFilesBadge,
      codeFilesTable
    ).render()

    val calcSrcCodeFiles = calculateSourceCodeFiles(spark, session, codeFilesTable, codeFilesBadge)
    calcSrcCodeFiles(())

    session.waitTillUserClosesSession()

def calculateSourceCodeFiles(spark: SparkSession, session: ConnectedSession, table: TableContainer, badge: Badge) =
  import spark.implicits.*
  import scala3encoders.given
  Calculation
    .newOutOnlyCalculation:
      createDatasetFromProjectsSourceFiles.toDS
    .whenStartingCalculationUpdateUi:
      table.withRowStringData(Nil)
      badge.text = "Calculating..."
      session.render()
    .whenCalculatedUpdateUi: tableData =>
      table.withRowStringData(tableData.take(10).toList.map(_.toData))
      badge.text = "Ready"
      session.render()
    .build
