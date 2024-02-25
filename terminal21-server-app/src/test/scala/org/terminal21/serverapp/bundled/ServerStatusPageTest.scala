package org.terminal21.serverapp.bundled

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar.mock
import org.terminal21.client.components.chakra.{Button, CheckIcon, NotAllowedIcon}
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
import org.terminal21.model.CommonModelBuilders.session
import org.terminal21.model.{CommonModelBuilders, Session}
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.serverapp.ServerSideSessions
import org.terminal21.client.given
import org.scalatest.matchers.should.Matchers.*

class ServerStatusPageTest extends AnyFunSuiteLike:
  class App:
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val sessionsService                      = mock[ServerSessionsService]
    val serverSideSessions                   = mock[ServerSideSessions]
    val page                                 = new ServerStatusPage(serverSideSessions, sessionsService)

  test("Close button for a session"):
    new App:
      page
        .sessionsTable(Seq(session()))
        .flat
        .collectFirst:
          case b: Button if b.text == "Close" => b
        .isEmpty should be(false)

  test("View state button for a session"):
    new App:
      page
        .sessionsTable(Seq(session()))
        .flat
        .collectFirst:
          case b: Button if b.text == "View State" => b
        .isEmpty should be(false)

  test("When session is open, a CheckIcon is displayed"):
    new App:
      page
        .sessionsTable(Seq(session()))
        .flat
        .collectFirst:
          case i: CheckIcon => i
        .isEmpty should be(false)

  test("When session is closed, a NotAllowedIcon is displayed"):
    new App:
      page
        .sessionsTable(Seq(session(isOpen = false)))
        .flat
        .collectFirst:
          case i: NotAllowedIcon => i
        .isEmpty should be(false)
