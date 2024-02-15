#!/usr/bin/env -S scala-cli --restart project.scala

/** note we use the --restart param for scala-cli. This means every time we change this file, scala-cli will terminate and rerun it with the changes. This way
  * we get the notebook feel when we use spark scripts.
  *
  * terminal21 spark lib caches datasets by storing them into disk. This way complex queries won't have to be re-evaluated on each restart of the script. We can
  * force re-evaluation by clicking the "Recalculate" buttons in the UI.
  */

// We need these imports
import org.apache.spark.sql.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.nivo.*
import org.terminal21.client.{*, given}
import org.terminal21.sparklib.*

import java.util.concurrent.atomic.AtomicInteger
import scala.util.{Random, Using}
import SparkNotebook.*
import org.terminal21.client.components.mathjax.{MathJax, MathJaxLib}
import org.terminal21.client.components.std.Paragraph

Using.resource(SparkSessions.newSparkSession( /* configure your spark session here */ )): spark =>
  Sessions
    .withNewSession("spark-notebook", "Spark Notebook")
    .andLibraries(NivoLib, MathJaxLib)
    .connect: session =>
      given ConnectedSession = session
      given SparkSession     = spark
      import scala3encoders.given
      import spark.implicits.*

      // lets get a Dataset, the data are random so that when we click refresh we can see the data actually
      // been refreshed.
      val peopleDS = createPeople

      // We will display the data in a table
      val peopleTable = QuickTable().headers("Id", "Name", "Age").caption("People")

      val peopleTableCalc = peopleDS
        .sort($"id")
        .visualize("People sample", peopleTable): data =>
          peopleTable.rows(data.take(5).map(p => Seq(p.id, p.name, p.age)))

      /** The calculation above uses a directory to store the dataset results. This way we can restart this script without loosing datasets that may take long
        * to calculate, making our script behave more like a notebook. When we click "Recalculate" in the UI, the cache directory is deleted and the dataset is
        * re-evaluated. If the Dataset schema changes, please click "Recalculate" or manually delete this folder.
        *
        * The key for the cache is "People sample"
        */
      println(s"Cache path: ${peopleTableCalc.cachePath}")

      val oldestPeopleChart = ResponsiveLine(
        axisBottom = Some(Axis(legend = "Person", legendOffset = 36)),
        axisLeft = Some(Axis(legend = "Age", legendOffset = -40)),
        legends = Seq(Legend())
      )

      val oldestPeopleChartCalc = peopleDS
        .orderBy($"age".desc)
        .visualize("Oldest people", oldestPeopleChart): data =>
          oldestPeopleChart.withData(
            Seq(
              Serie(
                "Person",
                data = data.take(5).map(person => Datum(person.name, person.age))
              )
            )
          )

      Seq(
        Paragraph(
          text = """
          |The spark notebooks can use the `visualise` extension method over a dataframe/dataset. It will cache the dataset by
          |saving it as a file under /tmp. The `Recalculate` button refreshes the dataset (re-runs it). In this example, the
          |data are random and so are different each time the `Recalculate` is pressed.
          |""".stripMargin,
          style = Map("margin" -> "32px")
        ),
        // just make it look a bit more like a proper notebook by adding some fake maths
        MathJax(
          expression = """
                       |The following is total nonsense but it simulates some explanation that would normally be here if we had
                       |a proper notebook. When \(a \ne 0\), there are two solutions to \(x = {-b \pm \sqrt{b^2-4ac} \over 2a}.\)
                       |Aenean vel velit a lacus lacinia pulvinar. Morbi eget ex et tellus aliquam molestie sit amet eu diam.
                       |Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tellus enim, tempor non efficitur et, rutrum efficitur metus.
                       |Nulla scelerisque, mauris sit amet accumsan iaculis, elit ipsum suscipit lorem, sed fermentum nunc purus non tellus.
                       |Aenean congue accumsan tempor. \(x = {-b \pm \sqrt{b^2-4ac} \over 2a}.\) maecenas vitae commodo tortor. Aliquam erat volutpat. Etiam laoreet malesuada elit sed vestibulum.
                       |Etiam consequat congue fermentum. Vivamus dapibus scelerisque ipsum eu tempus. Integer non pulvinar nisi.
                       |Morbi ultrices sem quis nisl convallis, ac cursus nunc condimentum. Orci varius natoque penatibus et magnis dis parturient montes,
                       |nascetur ridiculus mus.
                       |""".stripMargin,
          style = Map("margin" -> "32px")
        ),
        peopleTableCalc,
        oldestPeopleChartCalc
      ).render()

      session.waitTillUserClosesSession()

object SparkNotebook:
  private val names                 = Array("Andy", "Kostas", "Alex", "Andreas", "George", "Jack")
  private val surnames              = Array("Papadopoulos", "Rex", "Dylan", "Johnson", "Regan")
  def randomName: String            = names(Random.nextInt(names.length)) + " " + surnames(Random.nextInt(surnames.length))
  def randomPerson(id: Int): Person = Person(id, randomName + s" ($id)", Random.nextInt(100))

  def createPeople(using spark: SparkSession): Dataset[Person] =
    import spark.implicits.*
    import scala3encoders.given
    (1 to 100).toDS.map(randomPerson)
