package tests

import functions.fibers.Fiber
import org.terminal21.client.*

@main def runAll(): Unit =
  Seq(
    submit:
      chakraComponents()
    ,
    submit:
      stdComponents()
    ,
    submit:
      loginFormApp()
    ,
    submit:
      mathJaxComponents()
    ,
    submit:
      nivoComponents()
  ).foreach(_.get())

private def submit(f: => Unit): Fiber[Unit] =
  fiberExecutor.submit:
    try f
    catch case t: Throwable => t.printStackTrace()
