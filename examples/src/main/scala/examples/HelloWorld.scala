package examples

import org.terminal21.client.{Sessions, Std}

import java.util.UUID

@main def helloWorld(): Unit =
  val r = UUID.randomUUID().toString.substring(0, 4)
  Sessions.withNewSession(s"hello-world-$r", s"Hello World $r"): session =>
    println(session)
    val std = Std.newStd(session)
    std.header1("Big news!")
    std.paragraph(s"Hello there mr $r")
    for i <- 1 to 10 do
      std.paragraph(s"$i : $r - counting")
      Thread.sleep(1000)
