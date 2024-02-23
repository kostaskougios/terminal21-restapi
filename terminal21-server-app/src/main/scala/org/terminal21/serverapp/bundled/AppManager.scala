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
  given Model[ManagerModel] = Model(ManagerModel())

  def run(): Unit =
    eventsIterator.foreach(_ => ())

  val appRows: Seq[Seq[UiElement]] = apps.map: app =>
    Seq(
      Link(text = app.name).onClick: event =>
        import event.*
        handled.withModel(model.copy(startApp = Some(app)))
      ,
      Text(text = app.description)
    )

  def components =
    val appsTable = QuickTable(
      caption = Some("Apps installed on the server, click one to run it."),
      rows = appRows
    ).withHeaders("App Name", "Description")

    Seq(
      Header1(text = "Terminal 21 Manager"),
      Paragraph(
        text = """
                |Here you can run all the installed apps on the server.""".stripMargin
      ),
      appsTable,
      Paragraph().withChildren(
        Span(text = "Have a question? Please ask at "),
        Link(
          text = "terminal21's discussion board ",
          href = "https://github.com/kostaskougios/terminal21-restapi/discussions",
          color = Some("teal.500"),
          isExternal = Some(true)
        ).withChildren(ExternalLinkIcon(mx = Some("2px")))
      )
    )

  def controller(components: Seq[UiElement]): Controller[ManagerModel] =
    Controller(components)
      .onEvent: event =>
        import event.*
        // for every event, reset the model
        handled.withModel(model.copy(startApp = None))

  def controller: Controller[ManagerModel] = controller(components)

  def eventsIterator: Iterator[ManagerModel] =
    controller.eventsIterator
      .tapEach: m =>
        for app <- m.startApp do startApp(app)

trait AppManagerBeans:
  def serverSideSessions: ServerSideSessions
  def fiberExecutor: FiberExecutor
  def apps: Seq[ServerSideApp]
  def dependencies: Dependencies
  lazy val appManager = new AppManager(serverSideSessions, fiberExecutor, apps, dependencies)
