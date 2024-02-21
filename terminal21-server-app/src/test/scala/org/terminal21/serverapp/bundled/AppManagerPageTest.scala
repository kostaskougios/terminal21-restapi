package org.terminal21.serverapp.bundled

import org.mockito.Mockito
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar.mock
import org.terminal21.client.components.chakra.{Link, Text}
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
import org.terminal21.serverapp.ServerSideApp
import org.scalatest.matchers.should.Matchers.*

class AppManagerPageTest extends AnyFunSuiteLike:
  def mockApp(name: String, description: String) =
    val app = mock[ServerSideApp]
    when(app.name).thenReturn(name)
    when(app.description).thenReturn(description)
    app

  class App(apps: ServerSideApp*):
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val page                        = new AppManagerPage(apps, _ => ())
    def allComponents               = page.components.flatMap(_.flat)

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
