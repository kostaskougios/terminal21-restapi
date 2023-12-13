package examples

import org.terminal21.client.{Chakra, Sessions, Std}

import java.util.UUID
import java.util.logging.Logger

@main def helloWorld(): Unit =
  val logger = Logger.getLogger(getClass.getName)
  val r      = UUID.randomUUID().toString.substring(0, 4)
  logger.info(s"Session unique id = $r")
  Sessions.withNewSession(s"hello-world-$r", s"Hello World $r"): session =>
    println(session.session.id)

    val std    = session.use[Std]
    val chakra = session.use[Chakra]

    std.header1("Big news!", key = "header")
    std.paragraph(s"Hello there mr $r", key = "status")
    chakra.button("Click Me!"): () =>
      std.paragraph(s"Button clicked", key = "status")

    for i <- 1 to 100 do
      logger.info(s"at $i")
      std.paragraph(s"$r = $i", key = "progress")
      Thread.sleep(1000)

    std.header1("Done", key = "header")
