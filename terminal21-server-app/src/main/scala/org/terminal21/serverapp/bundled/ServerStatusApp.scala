package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.{ClientEvent, Session}
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
        new ServerStatusPage(serverSideSessions, dependencies.sessionsService)(using session, dependencies.fiberExecutor).run()

class ServerStatusPage(
    serverSideSessions: ServerSideSessions,
    sessionsService: ServerSessionsService
)(using appSession: ConnectedSession, fiberExecutor: FiberExecutor):
  import Model.Standard.unitModel

  case object Ticker extends ClientEvent

  def run(): Unit =
    fiberExecutor.submit:
      while !appSession.isClosed do
        Thread.sleep(2000)
        appSession.fireEvents(Ticker)
    controller(Runtime.getRuntime, sessionsService.allSessions).render().handledEventsIterator.lastOption

  private def toMb(v: Long) = s"${v / (1024 * 1024)} MB"
  private val xs            = Some("2xs")

  def controller(runtime: Runtime, sessions: => Seq[Session]): Controller[Unit] =
    Controller(components(runtime, sessions)).onEvent:
      case ControllerClientEvent(handled, Ticker) =>
        handled.withRenderChanges(sessionsTable(sessions))

  def components(runtime: Runtime, sessions: Seq[Session]): Seq[UiElement] =
    Seq(jvmTable(runtime), sessionsTable(sessions))

  def jvmTable(runtime: Runtime) = QuickTable(key = "jvmTable", caption = Some("JVM"))
    .withHeaders("Property", "Value", "Actions")
    .withRows(
      Seq(
        Seq("Free Memory", toMb(runtime.freeMemory()), ""),
        Seq("Max Memory", toMb(runtime.maxMemory()), ""),
        Seq(
          "Total Memory",
          toMb(runtime.totalMemory()),
          Button(size = xs, text = "Run GC").onClick: event =>
            System.gc()
            event.handled
        ),
        Seq("Available processors", runtime.availableProcessors(), "")
      )
    )

  def sessionsTable(sessions: Seq[Session]) = QuickTable(
    key = "sessions-table",
    caption = Some("All sessions"),
    rows = sessions.map: session =>
      Seq(Text(text = session.id), Text(text = session.name), if session.isOpen then CheckIcon() else NotAllowedIcon(), actionsFor(session))
  )
    .withHeaders("Id", "Name", "Is Open", "Actions")

  private def actionsFor(session: Session): UiElement =
    if session.isOpen then
      Box().withChildren(
        Button(text = "Close", size = xs)
          .withLeftIcon(SmallCloseIcon())
          .onClick: event =>
            import event.*
            sessionsService.terminateAndRemove(session)
            handled
        ,
        Text(text = " "),
        Button(text = "View State", size = xs)
          .withLeftIcon(ChatIcon())
          .onClick: event =>
            serverSideSessions
              .withNewSession(session.id + "-server-state", s"Server State:${session.id}")
              .connect: sSession =>
                new ViewServerStatePage(using sSession).runFor(sessionsService.sessionStateOf(session))
            event.handled
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

    val components = Seq(
      QuickTabs()
        .withTabs("Root Keys", "Key Tree")
        .withTabPanels(
          rootKeyPanel,
          keyTreePanel
        )
    )
    session.render(components)
    session.leaveSessionOpenAfterExiting()
