package examples

import org.terminal21.client.Sessions

import java.util.UUID

@main def helloWorld(): Unit =
  val r = UUID.randomUUID().toString.substring(0, 4)
  Sessions.withNewSession(s"hello-world-$r", s"Hello World $r"): session =>
    println(s"Session $session created")
