package org.terminal21.serverapp.bundled

import functions.fibers.FiberExecutor
import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.{Header1, Paragraph, Span}
import org.terminal21.model.SessionOptions
import org.terminal21.server.Dependencies
import org.terminal21.serverapp.{ServerSideApp, ServerSideSessions}

class AppManager(serverSideSessions: ServerSideSessions, fiberExecutor: FiberExecutor, apps: Seq[ServerSideApp], dependencies: Dependencies):
  def start(): Unit =
    fiberExecutor.submit:
      serverSideSessions
        .withNewSession("app-manager", "Terminal 21")
        .andOptions(SessionOptions(alwaysOpen = true))
        .connect: session =>
          given ConnectedSession = session
          new AppManagerPage(apps, startApp).run()

  private def startApp(app: ServerSideApp): Unit =
    fiberExecutor.submit:
      app.createSession(serverSideSessions, dependencies)

class AppManagerPage(apps: Seq[ServerSideApp], startApp: ServerSideApp => Unit)(using session: ConnectedSession):
  case class ManagerModel(startApp: Option[ServerSideApp] = None)

  def run(): Unit =
    eventsIterator.foreach(_ => ())

  def appRows(events: Events): Seq[MV[Option[ServerSideApp]]] = apps.map: app =>
    val link = Link(key = s"app-${app.name}", text = app.name)
    MV(
      if events.isClicked(link) then Some(app) else None,
      Box().withChildren(
        link,
        Text(text = app.description)
      )
    )

  def components(model: ManagerModel, events: Events): MV[ManagerModel] =
    val appsMv    = appRows(events)
    val appsTable = QuickTable(
      key = "apps-table",
      caption = Some("Apps installed on the server, click one to run it."),
      rows = appsMv.map(m => Seq(m.view))
    ).withHeaders("App Name", "Description")
    val startApp  = appsMv.map(_.model).find(_.nonEmpty).flatten
    MV(
      model.copy(startApp = startApp),
      Box().withChildren(
        Header1(text = "Terminal 21 Manager"),
        Paragraph(
          text = """
                    |Here you can run all the installed apps on the server.""".stripMargin
        ),
        appsTable,
        Paragraph().withChildren(
          Span(text = "Have a question? Please ask at "),
          Link(
            key = "discussion-board-link",
            text = "terminal21's discussion board ",
            href = "https://github.com/kostaskougios/terminal21-restapi/discussions",
            color = Some("teal.500"),
            isExternal = Some(true)
          ).withChildren(ExternalLinkIcon(mx = Some("2px")))
        )
      )
    )

  def controller: Controller[ManagerModel] =
    Controller(components)

  def eventsIterator: Iterator[ManagerModel] =
    controller
      .render(ManagerModel())
      .iterator
      .map(_.model)
      .tapEach: m =>
        for app <- m.startApp do startApp(app)

trait AppManagerBeans:
  def serverSideSessions: ServerSideSessions
  def fiberExecutor: FiberExecutor
  def apps: Seq[ServerSideApp]
  def dependencies: Dependencies
  lazy val appManager = new AppManager(serverSideSessions, fiberExecutor, apps, dependencies)
