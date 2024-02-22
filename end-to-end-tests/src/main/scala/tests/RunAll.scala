package tests

import org.terminal21.client.given

@main def runAll(): Unit =
  Seq(
    fiberExecutor.submit:
      chakraComponents()
    ,
    fiberExecutor.submit:
      stdComponents()
    ,
    fiberExecutor.submit:
      loginFormApp()
  ).foreach(_.get())
