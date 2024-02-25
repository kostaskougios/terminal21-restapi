package org.terminal21.sparklib.endtoend

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{Dataset, SparkSession}
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.nivo.*
import org.terminal21.client.{*, given}
import org.terminal21.sparklib.*
import org.terminal21.sparklib.endtoend.model.CodeFile
import org.terminal21.sparklib.endtoend.model.CodeFile.scanSourceFiles

import scala.util.Using

@main def sparkBasics(): Unit =
  Using.resource(SparkSessions.newSparkSession()): spark =>
    Sessions
      .withNewSession("spark-basics", "Spark Basics")
      .andLibraries(NivoLib)
      .connect: session =>
        given ConnectedSession = session
        given SparkSession     = spark
        given Model[Unit]      = Model.Standard.unitModel
        import scala3encoders.given
        import spark.implicits.*

        val headers = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate", "timestamp")

        val sortedFilesTable = QuickTable().withHeaders(headers: _*).caption("Files sorted by createdDate and numOfWords")
        val codeFilesTable   = QuickTable().withHeaders(headers: _*).caption("Unsorted files")

        val sortedSourceFilesDS = sortedSourceFiles(sourceFiles())
        val sortedCalc          = sortedSourceFilesDS.visualize("Sorted files", sortedFilesTable): results =>
          val tableRows = results.take(3).toList.map(_.toData)
          sortedFilesTable.withRows(tableRows)

        val codeFilesCalculation = sourceFiles().visualize("Code files", codeFilesTable): results =>
          val dt = results.take(3).toList
          codeFilesTable.withRows(dt.map(_.toData))

        val sortedFilesTableDF = QuickTable().withHeaders(headers: _*).caption("Files sorted by createdDate and numOfWords ASC and as DF")
        val sortedCalcAsDF     = sourceFiles()
          .sort($"createdDate".asc, $"numOfWords".asc)
          .toDF()
          .visualize("Sorted files DF", sortedFilesTableDF): results =>
            val tableRows = results.take(4).toList
            sortedFilesTableDF.withRows(tableRows.toUiTable)

        val chart = ResponsiveLine(
          data = Seq(
            Serie(
              "Scala",
              data = Seq(
                Datum("plane", 262),
                Datum("helicopter", 26),
                Datum("boat", 43)
              )
            )
          ),
          axisBottom = Some(Axis(legend = "Class", legendOffset = 36)),
          axisLeft = Some(Axis(legend = "Count", legendOffset = -40)),
          legends = Seq(Legend())
        )

        val sourceFileChart = sortedSourceFilesDS.visualize("Biggest Code Files", chart): results =>
          val data = results.take(10).map(cf => Datum(StringUtils.substringBeforeLast(cf.name, ".scala"), cf.numOfLines)).toList
          chart.withData(Seq(Serie("Scala", data = data)))

        Controller(
          Seq(
            codeFilesCalculation,
            sortedCalc,
            sortedCalcAsDF,
            sourceFileChart
          )
        ).lastEventOption

def sourceFiles()(using spark: SparkSession) =
  import scala3encoders.given
  import spark.implicits.*
  scanSourceFiles.toDS.map: cf =>
    cf.copy(timestamp = System.currentTimeMillis())

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc, $"numOfWords".desc)
