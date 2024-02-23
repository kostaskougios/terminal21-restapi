package org.terminal21.serverapp.bundled

import org.mockito.Mockito
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar.mock
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.{Link, Text}
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
import org.terminal21.serverapp.ServerSideApp
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.model.CommandEvent

class AppManagerPageTest extends AnyFunSuiteLike:
  def mockApp(name: String, description: String) =
    val app = mock[ServerSideApp]
    when(app.name).thenReturn(name)
    when(app.description).thenReturn(description)
    app

  class App(apps: ServerSideApp*):
    given session: ConnectedSession       = ConnectedSessionMock.newConnectedSessionMock
    var startedApp: Option[ServerSideApp] = None
    val page                              = new AppManagerPage(apps, app => startedApp = Some(app))
    def allComponents                     = page.components.flatMap(_.flat)

  test("renders app links"):
    new App(mockApp("app1", "the-app1-desc")):
      allComponents
        .collect:
          case l: Link if l.text == "app1" => l
        .size should be(1)

  test("renders app description"):
    new App(mockApp("app1", "the-app1-desc")):
      allComponents
        .collect:
          case t: Text if t.text == "the-app1-desc" => t
        .size should be(1)

  test("renders the discussions link"):
    new App():
      allComponents
        .collect:
          case l: Link if l.href == "https://github.com/kostaskougios/terminal21-restapi/discussions" => l
        .size should be(1)

  test("starts app when app link is clicked"):
    val app = mockApp("app1", "the-app1-desc")
    new App(app):
      val eventsIt = page.eventsIterator
      session.fireEvents(CommandEvent.onClick(page.appRows.head.head), CommandEvent.sessionClosed)
      eventsIt.toList
      startedApp should be(Some(app))

  test("resets startApp state on other events"):
    val app = mockApp("app1", "the-app1-desc")
    new App(app):
      val other    = Link()
      val eventsIt = page.controller(page.components :+ other).eventsIterator
      session.fireEvents(CommandEvent.onClick(page.appRows.head.head), CommandEvent.onClick(other), CommandEvent.sessionClosed)
      eventsIt.toList.last.startApp should be(None)
