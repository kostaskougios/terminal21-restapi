/** This build has different sections for each integration. I.e. an http4s section and a kafka section. These sections are not related to each other, please
  * examine the section you're interested in.
  */
val scala3Version = "3.3.1"

ThisBuild / version      := "0.1-SNAPSHOT"
ThisBuild / organization := "org.terminal21"
name                     := "rest-api"
ThisBuild / scalaVersion := scala3Version
ThisBuild / scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation")

// -----------------------------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------------------------

val FunctionsVersion       = "0.1-SNAPSHOT"
val FunctionsCaller        = "org.functions-remote" %% "functions-caller"   % FunctionsVersion
val FunctionsReceiver      = "org.functions-remote" %% "functions-receiver" % FunctionsVersion
val FunctionsAvro          = "org.functions-remote" %% "functions-avro"     % FunctionsVersion
val FunctionsHelidonServer = "org.functions-remote" %% "helidon-server"     % FunctionsVersion
val FunctionsHelidonClient = "org.functions-remote" %% "helidon-client"     % FunctionsVersion
val FunctionsFibers        = "org.functions-remote" %% "fibers"             % FunctionsVersion

val ScalaTest    = "org.scalatest" %% "scalatest" % "3.2.15" % Test
val CirceVersion = "0.14.6"
val Circe        = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % CirceVersion)

val HelidonVersion         = "4.0.1"
val HelidonWebServerHttp2  = "io.helidon.webserver" % "helidon-webserver-http2"          % HelidonVersion
val HelidonWebServerStatic = "io.helidon.webserver" % "helidon-webserver-static-content" % HelidonVersion
val HelidonWebSocket       = "io.helidon.webserver" % "helidon-webserver-websocket"      % HelidonVersion
val HelidonClient          = "io.helidon.webclient" % "helidon-webclient-http2"          % HelidonVersion
val HelidonServerLogging   = "io.helidon.logging"   % "helidon-logging-jul"              % HelidonVersion

val LogBack = Seq("ch.qos.logback" % "logback-classic" % "1.4.14")
// -----------------------------------------------------------------------------------------------
// Modules
// -----------------------------------------------------------------------------------------------

lazy val `helidon-server` = project
  .settings(
    libraryDependencies ++= Seq(
      FunctionsReceiver,
      FunctionsHelidonServer,
      ScalaTest,
      HelidonWebServerHttp2,
      HelidonWebSocket,
      HelidonWebServerStatic,
      FunctionsFibers,
      HelidonServerLogging % Test
    ) ++ Circe ++ LogBack
  )
  .dependsOn(`terminal21-ui-std-exports`)

lazy val `terminal21-ui-std-exports` = project
  .settings(
    libraryDependencies ++= Seq(ScalaTest),
    // make sure exportedArtifact points to the full artifact name of the receiver.
    buildInfoKeys    := Seq[BuildInfoKey](organization, name, version, scalaVersion, "exportedArtifact" -> "none"),
    buildInfoPackage := "org.terminal21.ui.std"
  )
  .enablePlugins(BuildInfoPlugin)

lazy val `terminal21-ui-std` = project
  .settings(
    callerExports                := Seq(s"org.terminal21:terminal21-ui-std-exports_3:${version.value}"),
    callerJsonSerialization      := true,
    callerHelidonClientTransport := true,
    libraryDependencies ++= Seq(ScalaTest, HelidonClient, FunctionsCaller, FunctionsHelidonClient) ++ Circe
  )
  .dependsOn(`terminal21-ui-std-exports`)
  .enablePlugins(FunctionsRemotePlugin)
