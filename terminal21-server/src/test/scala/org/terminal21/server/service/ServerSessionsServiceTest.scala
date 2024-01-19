package org.terminal21.server.service

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.model.SessionClosed
import org.terminal21.ui.std.StdExportsBuilders.serverJson

class ServerSessionsServiceTest extends AnyFunSuiteLike:
  test("sessionById"):
    new App:
      val session = createSession
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.sessionById(session.id) should be(session)

  test("sessionStateOf"):
    new App:
      val session = createSession
      val sj      = serverJson()
      serverSessionsService.setSessionJsonState(session, sj)
      serverSessionsService.sessionStateOf(session).serverJson should be(sj)

  test("removeSession"):
    new App:
      val session = createSession
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.removeSession(session)
      an[IllegalArgumentException] should be thrownBy:
        serverSessionsService.sessionById(session.id)

  test("removeSession notifies listeners"):
    new App:
      val session        = createSession
      serverSessionsService.setSessionJsonState(session, serverJson())
      var listenerCalled = 0
      serverSessionsService.notifyMeWhenSessionsChange: sessions =>
        listenerCalled match
          case 0 => sessions should be(Seq(session))
          case 1 => sessions should be(Nil)
        listenerCalled += 1
        true
      serverSessionsService.removeSession(session)
      listenerCalled should be(2)

  test("terminateSession marks session as closed"):
    new App:
      val session = createSession
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.terminateSession(session)
      serverSessionsService.sessionById(session.id).isOpen should be(false)

  test("terminateSession notifies session listeners"):
    new App:
      val session     = createSession
      serverSessionsService.setSessionJsonState(session, serverJson())
      var eventCalled = false
      serverSessionsService.notifyMeOnSessionEvents(session): event =>
        event should be(SessionClosed("-"))
        eventCalled = true
        true
      serverSessionsService.terminateSession(session)
      eventCalled should be(true)

  test("terminateSession notifies sessions listeners"):
    new App:
      val session        = createSession
      serverSessionsService.setSessionJsonState(session, serverJson())
      var listenerCalled = 0
      serverSessionsService.notifyMeWhenSessionsChange: sessions =>
        listenerCalled match
          case 0 => sessions should be(Seq(session))
          case 1 => sessions should be(Seq(session.close))
        listenerCalled += 1
        true
      serverSessionsService.terminateSession(session)
      listenerCalled should be(2)

  test("createSession notifies listeners"):
    new App:
      var listenerCalled = 0
      serverSessionsService.notifyMeWhenSessionsChange: sessions =>
        listenerCalled match
          case 0 => sessions should be(Nil)
          case 1 => sessions.size should be(1)
        listenerCalled += 1
        true

      val session = createSession
      listenerCalled should be(2)

  class App:
    val serverSessionsService = new ServerSessionsService
    def createSession         = serverSessionsService.createSession("test", "Test")
