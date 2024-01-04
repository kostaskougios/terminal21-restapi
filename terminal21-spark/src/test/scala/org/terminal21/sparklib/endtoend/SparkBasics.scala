package org.terminal21.sparklib.endtoend

import org.apache.spark.sql.{Dataset, SparkSession}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.{*, given}
import org.terminal21.sparklib.SparkSessions
import org.terminal21.sparklib.endtoend.model.CodeFile
import org.terminal21.sparklib.endtoend.model.CodeFile.createDatasetFromProjectsSourceFiles
import org.terminal21.sparklib.steps.StepComponent

@main def sparkBasics(): Unit =
  SparkSessions.newTerminal21WithSparkSession(SparkSessions.newSparkSession(), "spark-basics", "Spark Basics"): (spark, session) =>
    given ConnectedSession = session
    given SparkSession     = spark

    import scala3encoders.given
    import spark.implicits.*

    val headers = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate")

    val codeFilesTable     = QuickTable.quickTable().withStringHeaders(headers: _*).build
    val codeFilesComponent = StepComponent.stdStep("Code files", codeFilesTable)

    val sortedFilesTable = QuickTable.quickTable().withStringHeaders(headers: _*).build
    val sortedFilesBadge = Badge()

    Seq(
      codeFilesComponent,
      Box(text = "Code files sorted by date", bg = "green", p = 4),
      sortedFilesBadge,
      sortedFilesTable
    ).render()

    val sortedCalc = Calculation
      .newCalculation(sortedSourceFiles)
      .whenStartingCalculationUpdateUi:
        sortedFilesBadge.text = "Calculating..."
        session.render()
      .whenCalculatedUpdateUi: data =>
        sortedFilesBadge.text = "Ready"
        sortedFilesTable.withRowStringData(data.take(10).toList.map(_.toData))
        session.render()
      .build

    Calculation
      .newCalculationNoIn:
        createDatasetFromProjectsSourceFiles.toDS
      .whenStartingCalculationUpdateUi:
        sortedFilesTable.withRowStringData(Nil)
        codeFilesComponent.calculating()
      .whenCalculatedUpdateUi: tableData =>
        val dt = tableData.take(10).toList
        codeFilesTable.withRowStringData(dt.map(_.toData))
        codeFilesComponent.ready()
      .notifyAfterCalculated(sortedCalc)
      .build
      .run(())

    session.waitTillUserClosesSession()

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc)
