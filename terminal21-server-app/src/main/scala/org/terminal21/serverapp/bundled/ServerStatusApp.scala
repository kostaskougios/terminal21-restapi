package org.terminal21.serverapp.bundled

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.Session
import org.terminal21.server.Dependencies
import org.terminal21.server.model.SessionState
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class ServerStatusApp extends ServerSideApp:
  override def name: String        = "Server Status"
  override def description: String = "Status of the server."

  override def createSession(serverSideSessions: ServerSideSessions, dependencies: Dependencies): Unit =
    serverSideSessions
      .withNewSession("server-status", "Server Status")
      .connect: session =>
        new ServerStatusPage(serverSideSessions, dependencies.sessionsService)(using session).run()

class ServerStatusPage(
    serverSideSessions: ServerSideSessions,
    sessionsService: ServerSessionsService
)(using session: ConnectedSession):
  def run(): Unit =
    while !session.isClosed do
      updateStatus()
      Thread.sleep(1000)

  private def toMb(v: Long)        = s"${v / (1024 * 1024)} MB"
  private val xs                   = Some("2xs")
  private def updateStatus(): Unit =
    components(Runtime.getRuntime, sessionsService.allSessions).render()

  def components(runtime: Runtime, sessions: Seq[Session]): Seq[UiElement] =
    val jvmTable      = QuickTable(caption = Some("JVM"))
      .withHeaders("Property", "Value", "Actions")
      .withRows(
        Seq(
          Seq("Free Memory", toMb(runtime.freeMemory()), ""),
          Seq("Max Memory", toMb(runtime.maxMemory()), ""),
          Seq(
            "Total Memory",
            toMb(runtime.totalMemory()),
            Button(size = xs, text = "Run GC").onClick: () =>
              System.gc()
          ),
          Seq("Available processors", runtime.availableProcessors(), "")
        )
      )
    val sessionsTable = QuickTable(
      caption = Some("All sessions"),
      rows = sessions.map: session =>
        Seq(Text(text = session.id), Text(text = session.name), if session.isOpen then CheckIcon() else NotAllowedIcon(), actionsFor(session))
    )
      .withHeaders("Id", "Name", "Is Open", "Actions")

    Seq(jvmTable, sessionsTable)

  private def actionsFor(session: Session)(using ConnectedSession): UiElement =
    if session.isOpen then
      Box().withChildren(
        Button(text = "Close", size = xs)
          .withLeftIcon(SmallCloseIcon())
          .onClick: () =>
            sessionsService.terminateAndRemove(session)
            updateStatus()
        ,
        Text(text = " "),
        Button(text = "View State", size = xs)
          .withLeftIcon(ChatIcon())
          .onClick: () =>
            serverSideSessions
              .withNewSession(session.id + "-server-state", s"Server State:${session.id}")
              .connect: sSession =>
                new ViewServerStatePage(using sSession).runFor(sessionsService.sessionStateOf(session))
      )
    else NotAllowedIcon()

class ViewServerStatePage(using session: ConnectedSession):

  def runFor(state: SessionState): Unit =
    val sj = state.serverJson

    val rootKeyPanel = Seq(
      QuickTable()
        .withCaption("Root Keys")
        .withHeaders("Root Key")
        .withRows(
          sj.rootKeys.sorted.map(k => Seq(k))
        )
    )

    val keyTreePanel = Seq(
      QuickTable()
        .withCaption("Key Tree")
        .withHeaders("Key", "Component Json", "Children")
        .withRows(
          sj.keyTree.toSeq.sortBy(_._1).map((k, v) => Seq(k, sj.elements(k).noSpaces, v.mkString(", ")))
        )
    )

    Seq(
      QuickTabs()
        .withTabs("Root Keys", "Key Tree")
        .withTabPanels(
          rootKeyPanel,
          keyTreePanel
        )
    ).render()
    session.waitTillUserClosesSession()
