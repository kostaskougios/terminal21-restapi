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
  case class StatusModel(runtime: Runtime, sessions: Seq[Session])
  val initModel            = StatusModel(Runtime.getRuntime, sessionsService.allSessions)
  given Model[StatusModel] = Model(initModel)

  case class Ticker(sessions: Seq[Session]) extends ClientEvent

  def run(): Unit =
    fiberExecutor.submit:
      while !appSession.isClosed do
        Thread.sleep(2000)
        appSession.fireEvents(Ticker(sessionsService.allSessions))

    try controller.render().handledEventsIterator.lastOption
    catch case t: Throwable => t.printStackTrace()

  private def toMb(v: Long) = s"${v / (1024 * 1024)} MB"
  private val xs            = Some("2xs")

  def controller: Controller[StatusModel] =
    Controller(components).onEvent:
      case ControllerClientEvent(handled, Ticker(sessions)) =>
        handled.withModel(handled.model.copy(sessions = sessions))

  def components(m: StatusModel): Seq[UiElement] =
    Seq(jvmTable(m.runtime), sessionsTable(m.sessions))

  private val jvmTableE = QuickTable(key = "jvmTable", caption = Some("JVM"))
    .withHeaders("Property", "Value", "Actions")
  private val gcButton  = Button(size = xs, text = "Run GC")
    .onClick: event =>
      System.gc()
      event.handled

  def jvmTable(runtime: Runtime) =
    jvmTableE.withRows(
      Seq(
        Seq("Free Memory", toMb(runtime.freeMemory()), ""),
        Seq("Max Memory", toMb(runtime.maxMemory()), ""),
        Seq("Total Memory", toMb(runtime.totalMemory()), gcButton),
        Seq("Available processors", runtime.availableProcessors(), "")
      )
    )

  private val sessionsTableE =
    QuickTable(
      key = "sessions-table",
      caption = Some("All sessions")
    ).withHeaders("Id", "Name", "Is Open", "Actions")

  def sessionsTable(sessions: Seq[Session]) = sessionsTableE.withRows(
    sessions.map: session =>
      Seq(Text(text = session.id), Text(text = session.name), if session.isOpen then CheckIcon() else NotAllowedIcon(), actionsFor(session))
  )

  private def actionsFor(session: Session): UiElement =
    if session.isOpen then
      Box(key = s"session-${session.id}-actions").withChildren(
        Button(key = s"close-${session.id}", text = "Close", size = xs)
          .withLeftIcon(SmallCloseIcon())
          .onClick: event =>
            import event.*
            sessionsService.terminateAndRemove(session)
            handled
        ,
        Text(text = " "),
        Button(key = s"view-${session.id}", text = "View State", size = xs)
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
