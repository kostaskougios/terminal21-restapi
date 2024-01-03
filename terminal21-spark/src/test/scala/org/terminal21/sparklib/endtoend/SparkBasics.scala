package org.terminal21.sparklib.endtoend

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.sparklib.SparkSessions
import org.terminal21.sparklib.endtoend.model.CodeFile.createDatasetFromProjectsSourceFiles
import org.terminal21.sparklib.steps.Steps

import java.time.LocalDate

@main def sparkBasics(): Unit =
  SparkSessions.newTerminal21WithSparkSession(SparkSessions.newSparkSession(), "spark-basics", "Spark Basics"): (spark, session) =>
    given ConnectedSession = session

    import scala3encoders.given
    import spark.implicits.*
    val steps = Steps(spark, "spark-basics")

    val step1 = steps.step("query-dataset")
    println(step1.targetDir)

    val sourceCodeDs = step1.calculateOnce:
      createDatasetFromProjectsSourceFiles.toDS

    val tableData = sourceCodeDs.take(10).toList
    Seq(
      QuickTable.quickTable(Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate"), tableData.map(d => d.toData.map(c => Text(text = c))))
    ).render()
    session.waitTillUserClosesSession()
