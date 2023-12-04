package examples

import org.terminal21.client.Sessions

@main def helloWorld(): Unit =
  Sessions.withNewSession("hello-world", "Hello World"): session =>
    println(s"Session $session created")
