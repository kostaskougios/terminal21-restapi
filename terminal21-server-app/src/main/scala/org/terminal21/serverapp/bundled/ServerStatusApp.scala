package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Paragraph
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
  val initModel = StatusModel(Runtime.getRuntime, sessionsService.allSessions)

  case class Ticker(sessions: Seq[Session]) extends ClientEvent

  def run(): Unit =
    fiberExecutor.submit:
      while !appSession.isClosed do
        Thread.sleep(2000)
        appSession.fireEvents(Ticker(sessionsService.allSessions))

    try controller.render(initModel).iterator.lastOption
    catch case t: Throwable => t.printStackTrace()

  private def toMb(v: Long) = s"${v / (1024 * 1024)} MB"
  private val xs            = Some("2xs")

  def controller: Controller[StatusModel] =
    Controller(components)

  def components(model: StatusModel, events: Events): MV[StatusModel] =
    MV(
      model,
      Box().withChildren(
        jvmTable(model.runtime, events),
        sessionsTable(model.sessions, events)
      )
    )

  private val jvmTableE = QuickTable(key = "jvmTable", caption = Some("JVM"))
    .withHeaders("Property", "Value", "Actions")
  private val gcButton  = Button(key = "gc-button", size = xs, text = "Run GC")

  def jvmTable(runtime: Runtime, events: Events): UiElement =
    if events.isClicked(gcButton) then System.gc()
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

  def sessionsTable(sessions: Seq[Session], events: Events): UiElement =
    sessionsTableE.withRows(
      sessions.map: session =>
        Seq(Text(text = session.id), Text(text = session.name), if session.isOpen then CheckIcon() else NotAllowedIcon(), actionsFor(session, events))
    )

  private def actionsFor(session: Session, events: Events): UiElement =
    if session.isOpen then
      val closeButton = Button(key = s"close-${session.id}", text = "Close", size = xs)
        .withLeftIcon(SmallCloseIcon())
      if events.isClicked(closeButton) then sessionsService.terminateAndRemove(session)
      val viewButton  = Button(key = s"view-${session.id}", text = "View State", size = xs)
        .withLeftIcon(ChatIcon())
      if events.isClicked(viewButton) then
        serverSideSessions
          .withNewSession(session.id + "-server-state", s"Server State:${session.id}")
          .connect: sSession =>
            new ViewServerStatePage(using sSession).runFor(sessionsService.sessionStateOf(session))

      Box().withChildren(
        closeButton,
        Text(text = " "),
        viewButton
      )
    else NotAllowedIcon()

class ViewServerStatePage(using session: ConnectedSession):

  def runFor(state: SessionState): Unit =
    val sj = state.serverJson

    val components = Seq(
      QuickTabs()
        .withTabs("Json")
        .withTabPanels(
          Seq(Paragraph(text = sj.toString))
        )
    )
    session.render(components)
    session.leaveSessionOpenAfterExiting()
