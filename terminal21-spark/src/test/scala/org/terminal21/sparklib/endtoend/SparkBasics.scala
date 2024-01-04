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

    val steps = Steps(spark, "spark-basics")
    val step1 = steps.step("query-dataset")

    val codeFilesTable = QuickTable.quickTable().withStringHeaders("id", "name", "path", "numOfLines", "numOfWords", "createdDate").build

    Seq(
      Button(text = "Code files").onClick: () =>
        step1.invalidateCache(),
      codeFilesTable
    ).render()

    val calcSrcCodeFiles = calculateSourceCodeFiles(spark, session, codeFilesTable)
    calcSrcCodeFiles(())

    session.waitTillUserClosesSession()

def calculateSourceCodeFiles(spark: SparkSession, session: ConnectedSession, codeFilesTable: TableContainer): Calculation[Unit, Dataset[CodeFile]] =
  import spark.implicits.*
  import scala3encoders.given
  Calculation(
    _ => createDatasetFromProjectsSourceFiles.toDS.orderBy($"createdDate".desc),
    () =>
      codeFilesTable.withRowStringData(Nil)
      session.render()
    ,
    tableData =>
      codeFilesTable.withRowStringData(tableData.take(10).map(_.toData))
      session.render()
  )
