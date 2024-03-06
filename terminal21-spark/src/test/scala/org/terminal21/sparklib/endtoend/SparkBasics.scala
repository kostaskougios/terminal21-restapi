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
        import scala3encoders.given
        import spark.implicits.*

        val sourceFileCached          = Cached("Code files"):
          sourceFiles().limit(3)
        val sortedSourceFilesDS       = Cached("Sorted files"):
          sortedSourceFiles(sourceFiles()).limit(3)
        val sortedSourceFilesDFCached = Cached("Sorted files DF"):
          sourceFiles()
            .sort($"createdDate".asc, $"numOfWords".asc)
            .toDF()
            .limit(4)

        val sourceFilesSortedByNumOfLinesCached = Cached("Biggest Code Files"):
          sourceFiles()
            .sort($"numOfLines".desc)

        println(s"Cached dir: ${sourceFileCached.cachePath}")
        def components(events: Events) =
          println("components() START")
          given Events = events

          val headers          = Seq("id", "name", "path", "numOfLines", "numOfWords", "createdDate", "timestamp")
          val sortedFilesTable = QuickTable().withHeaders(headers: _*).caption("Files sorted by createdDate and numOfWords")
          val codeFilesTable   = QuickTable().withHeaders(headers: _*).caption("Unsorted files")

          val sortedCalc = sortedSourceFilesDS.visualize(sortedFilesTable): results =>
            val tableRows = results.collect().map(_.toData).toList
            sortedFilesTable.withRows(tableRows)

          val codeFilesCalculation = sourceFileCached.visualize(codeFilesTable): results =>
            val dt = results.collect().toList
            codeFilesTable.withRows(dt.map(_.toData))

          val sortedFilesTableDF = QuickTable().withHeaders(headers: _*).caption("Files sorted by createdDate and numOfWords ASC and as DF")
          val sortedCalcAsDF     = sortedSourceFilesDFCached
            .visualize(sortedFilesTableDF): results =>
              val tableRows = results.collect().toList
              sortedFilesTableDF.withRows(tableRows.toUiTable)

          val chart = ResponsiveLine(
            data = Seq(
              Serie(
                "Scala",
                data = Nil
              )
            ),
            axisBottom = Some(Axis(legend = "Class", legendOffset = 36)),
            axisLeft = Some(Axis(legend = "Number of Lines", legendOffset = -40)),
            legends = Seq(Legend())
          )

          val sourceFileChart = sourceFilesSortedByNumOfLinesCached
            .visualize(chart): results =>
              val data = results.take(10).map(cf => Datum(StringUtils.substringBeforeLast(cf.name, ".scala"), cf.numOfLines)).toList
              chart.withData(Seq(Serie("Scala", data = data)))
          println("components() RETURNING")
          Seq(
            codeFilesCalculation,
            sortedCalc,
            sortedCalcAsDF,
            sourceFileChart
          )

        Controller
          .noModel(components)
          .render()
          .run()

def sourceFiles()(using spark: SparkSession) =
  import scala3encoders.given
  import spark.implicits.*
  scanSourceFiles.toDS.map: cf =>
    cf.copy(timestamp = System.currentTimeMillis())

def sortedSourceFiles(sourceFiles: Dataset[CodeFile])(using spark: SparkSession) =
  import spark.implicits.*
  sourceFiles.sort($"createdDate".desc, $"numOfWords".desc)
