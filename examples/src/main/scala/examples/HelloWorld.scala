package examples

import org.terminal21.client.{Chakra, Sessions, Std}

import java.util.UUID

@main def helloWorld(): Unit =
  val r = UUID.randomUUID().toString.substring(0, 4)
  Sessions.withNewSession(s"hello-world-$r", s"Hello World $r"): session =>
    println(session.session.id)

    val std    = session.use[Std]
    val chakra = session.use[Chakra]

    std.header1("Big news!", key = "header")
    std.paragraph(s"Hello there mr $r")
    chakra.button("Click Me!")
    for i <- 1 to 100 do
      std.paragraph(s"$r = $i", key = "progress")
      Thread.sleep(1000)

    std.header1("Done", key = "header")
