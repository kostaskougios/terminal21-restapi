package org.terminal21.serverapp.bundled

import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar.mock
import org.terminal21.client.components.chakra.{Button, CheckIcon, NotAllowedIcon, Text}
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
import org.terminal21.model.CommonModelBuilders.session
import org.terminal21.model.{CommandEvent, CommonModelBuilders, Session}
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.serverapp.ServerSideSessions
import org.terminal21.client.given
import org.scalatest.matchers.should.Matchers.*

class ServerStatusPageTest extends AnyFunSuiteLike:
  class App:
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val sessionsService                      = mock[ServerSessionsService]
    val serverSideSessions                   = mock[ServerSideSessions]
    when(sessionsService.allSessions).thenReturn(Seq(session(id = "session1")))
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

  test("sessions are rendered when Ticker event is fired"):
    new App:
      val it                = page.controller.render().handledEventsIterator
      private val sessions2 = Seq(session(id = "s2", name = "session 2"))
      private val sessions3 = Seq(session(id = "s3", name = "session 3"))
      connectedSession.fireEvents(
        page.Ticker(sessions2),
        page.Ticker(sessions3),
        CommandEvent.sessionClosed
      )
      val handledEvents     = it.toList
      handledEvents.head.model.sessions should be(Seq(session(id = "session1")))
      handledEvents(1).model.sessions should be(sessions2)
      handledEvents(2).model.sessions should be(sessions3)
