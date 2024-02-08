package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.Session
import org.terminal21.server.Dependencies
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class ServerStatusApp extends ServerSideApp:
  override def name: String        = "Server Status"
  override def description: String = "Status of the server."

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions
      .withNewSession("server-status", "Server Status")
      .connect: session =>
        given ConnectedSession = session
        new ServerStatusAppInternal(dependencies.sessionsService, dependencies.fiberExecutor).run()

class ServerStatusAppInternal(sessionsService: ServerSessionsService, executor: FiberExecutor)(using session: ConnectedSession):
  def run(): Unit =
    executor.submit:
      while !session.isClosed do
        updateStatus()
        Thread.sleep(1000)
    session.waitTillUserClosesSession()

  private def toMb(v: Long) = s"${v / (1024 * 1024)} MB"

  private def updateStatus(): Unit =
    val runtime = Runtime.getRuntime

    val jvmTable      = QuickTable(caption = Some("JVM"))
      .headers("Property", "Value", "Actions")
      .rows(
        Seq(
          Seq("Free Memory", toMb(runtime.freeMemory()), ""),
          Seq("Max Memory", toMb(runtime.maxMemory()), ""),
          Seq(
            "Total Memory",
            toMb(runtime.totalMemory()),
            Button(size = Some("2xs"), text = "Run GC").onClick: () =>
              System.gc()
          ),
          Seq("Available processors", runtime.availableProcessors(), "")
        )
      )
    val sessions      = sessionsService.allSessions
    val sessionsTable = QuickTable(
      caption = Some("All sessions"),
      rows = sessions.map: session =>
        Seq(Text(text = session.id), Text(text = session.name), if session.isOpen then CheckIcon() else NotAllowedIcon(), actionsFor(session))
    )
      .headers("Id", "Name", "Is Open", "Actions")

    Seq(jvmTable, sessionsTable).render()

  private def actionsFor(session: Session)(using ConnectedSession): UiElement =
    if session.isOpen then
      Button(text = "Close", size = Some("sm"))
        .withLeftIcon(CloseIcon())
        .onClick: () =>
          sessionsService.terminateAndRemove(session)
          updateStatus()
    else NotAllowedIcon()
