/** This build has different sections for each integration. I.e. an http4s section and a kafka section. These sections are not related to each other, please
  * examine the section you're interested in.
  */
val scala3Version = "3.3.1"

ThisBuild / version      := "0.30"
ThisBuild / organization := "io.github.kostaskougios"
name                     := "rest-api"
ThisBuild / scalaVersion := scala3Version
ThisBuild / scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation", "-Xmax-inlines", "256")

ThisBuild / resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

// -----------------------------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------------------------

val FunctionsVersion         = "0.51"
val FunctionsCaller          = "io.github.kostaskougios" %% "functions-caller"   % FunctionsVersion
val FunctionsReceiver        = "io.github.kostaskougios" %% "functions-receiver" % FunctionsVersion
val FunctionsAvro            = "io.github.kostaskougios" %% "functions-avro"     % FunctionsVersion
val FunctionsHelidonServer   = "io.github.kostaskougios" %% "helidon-server"     % FunctionsVersion
val FunctionsHelidonWsServer = "io.github.kostaskougios" %% "helidon-ws-server"  % FunctionsVersion
val FunctionsHelidonClient   = "io.github.kostaskougios" %% "helidon-client"     % FunctionsVersion
val FunctionsHelidonWsClient = "io.github.kostaskougios" %% "helidon-ws-client"  % FunctionsVersion
val FunctionsFibers          = "io.github.kostaskougios" %% "fibers"             % FunctionsVersion

val ScalaTest   = "org.scalatest"     %% "scalatest"              % "3.2.18"     % Test
val Mockito     = "org.mockito"        % "mockito-all"            % "2.0.2-beta" % Test
val Mockito510  = "org.scalatestplus" %% "mockito-5-10"           % "3.2.18.0"   % Test
val Scala3Tasty = "org.scala-lang"    %% "scala3-tasty-inspector" % scala3Version
val CommonsText = "org.apache.commons" % "commons-text"           % "1.10.0"
val CommonsIO   = "commons-io"         % "commons-io"             % "2.11.0"

val CirceVersion = "0.14.6"
val Circe        = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % CirceVersion)

val HelidonVersion         = "4.0.2"
val HelidonWebServerHttp2  = "io.helidon.webserver" % "helidon-webserver-http2"          % HelidonVersion
val HelidonWebServerStatic = "io.helidon.webserver" % "helidon-webserver-static-content" % HelidonVersion
val HelidonServerWebSocket = "io.helidon.webserver" % "helidon-webserver-websocket"      % HelidonVersion
val HelidonClientWebSocket = "io.helidon.webclient" % "helidon-webclient-websocket"      % HelidonVersion
val HelidonClient          = "io.helidon.webclient" % "helidon-webclient-http2"          % HelidonVersion
val HelidonServerLogging   = "io.helidon.logging"   % "helidon-logging-jul"              % HelidonVersion

val LogBack  = "ch.qos.logback" % "logback-classic" % "1.4.14"
val Slf4jApi = "org.slf4j"      % "slf4j-api"       % "2.0.9"

val SparkSql = ("org.apache.spark" %% "spark-sql" % "3.5.0" % "provided").cross(CrossVersion.for3Use2_13).exclude("org.scala-lang.modules", "scala-xml_2.13")
val SparkScala3Fix = Seq(
  "io.github.vincenzobaz" %% "spark-scala3-encoders" % "0.2.5",
  "io.github.vincenzobaz" %% "spark-scala3-udf"      % "0.2.5"
).map(_.exclude("org.scala-lang.modules", "scala-xml_2.13"))

// -----------------------------------------------------------------------------------------------
// Modules
// -----------------------------------------------------------------------------------------------
val commonSettings = Seq(
  Test / fork := true,
  Test / javaOptions += "--add-opens=java.base/java.lang=ALL-UNNAMED"
)

lazy val `terminal21-server-client-common` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Circe ++ Seq(
      ScalaTest,
      Slf4jApi,
      HelidonClientWebSocket,
      FunctionsFibers,
      HelidonWebServerHttp2  % Test,
      HelidonServerWebSocket % Test,
      LogBack                % Test
    )
  )

lazy val `terminal21-server` = project
  .settings(
    commonSettings,
    receiverExports           := Seq(s"io.github.kostaskougios:terminal21-ui-std-exports_3:${version.value}"),
    receiverJsonSerialization := true,
    receiverHelidonRoutes     := true,
    libraryDependencies ++= Seq(
      FunctionsReceiver,
      ScalaTest,
      Mockito,
      HelidonWebServerHttp2,
      HelidonServerWebSocket,
      HelidonWebServerStatic,
      FunctionsHelidonServer,
      FunctionsHelidonWsServer,
      FunctionsFibers,
      HelidonServerLogging % Test,
      LogBack
    ) ++ Circe
  )
  .dependsOn(`terminal21-ui-std-exports` % "compile->compile;test->test", `terminal21-server-client-common`)
  .enablePlugins(FunctionsRemotePlugin)

lazy val `terminal21-server-app` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      Mockito510
    )
  )
  .dependsOn(
    `terminal21-server`               % "compile->compile;test->test",
    `terminal21-ui-std`               % "compile->compile;test->test",
    `terminal21-server-client-common` % "compile->compile;test->test"
  )

lazy val `terminal21-ui-std-exports` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(ScalaTest) ++ Circe,
    // make sure exportedArtifact points to the full artifact name of the receiver.
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "none"),
    buildInfoPackage := "org.terminal21.ui.std"
  )
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(`terminal21-server-client-common`)

lazy val `terminal21-client-common` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(ScalaTest, Mockito, FunctionsFibers, HelidonClientWebSocket) ++ Circe
  )
  .dependsOn(`terminal21-server-client-common`)

lazy val `terminal21-ui-std` = project
  .settings(
    commonSettings,
    callerExports                := Seq(s"io.github.kostaskougios:terminal21-ui-std-exports_3:${version.value}"),
    callerJsonSerialization      := true,
    callerHelidonClientTransport := true,
    libraryDependencies ++= Seq(
      ScalaTest,
      Mockito,
      Mockito510,
      Slf4jApi,
      HelidonClient,
      FunctionsCaller,
      FunctionsHelidonClient,
      FunctionsHelidonWsClient,
      HelidonClientWebSocket
    ) ++ Circe
  )
  .dependsOn(
    `terminal21-ui-std-exports`,
    `terminal21-server-client-common` % "compile->compile;test->test",
    `terminal21-client-common`        % "compile->compile;test->test"
  )
  .enablePlugins(FunctionsRemotePlugin)

lazy val `end-to-end-tests` = project
  .settings(
    commonSettings,
    publish := {},
    libraryDependencies ++= Seq(ScalaTest, LogBack)
  )
  .dependsOn(`terminal21-ui-std` % "compile->compile;test->test", `terminal21-nivo`, `terminal21-mathjax`)

lazy val `terminal21-nivo` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      ScalaTest
    )
  )
  .dependsOn(`terminal21-ui-std` % "compile->compile;test->test")

lazy val `terminal21-spark` = project
  .settings(
    commonSettings,
    Test / fork := true,
    Test / javaOptions ++= Seq("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED"),
    libraryDependencies ++= Seq(
      ScalaTest,
      SparkSql,
      LogBack % Test
    ) ++ SparkScala3Fix
  )
  .dependsOn(`terminal21-ui-std` % "compile->compile;test->test", `terminal21-nivo` % Test)

lazy val `terminal21-mathjax` = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      ScalaTest
    )
  )
  .dependsOn(`terminal21-ui-std` % "compile->compile;test->test")

lazy val `terminal21-code-generation`: Project = project
  .settings(
    commonSettings,
    publish := {},
    libraryDependencies ++= Seq(
      ScalaTest,
      Scala3Tasty,
      CommonsText,
      CommonsIO
    )
  )
