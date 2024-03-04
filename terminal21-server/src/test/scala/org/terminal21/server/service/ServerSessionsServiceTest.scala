package org.terminal21.server.service

import io.circe.Json
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.model.{OnChange, OnClick, SessionClosed, SessionOptions}
import org.terminal21.server.json
import org.terminal21.ui.std.StdExportsBuilders.serverJson

class ServerSessionsServiceTest extends AnyFunSuiteLike:
  test("sessionById"):
    new App:
      val session = createSession()
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.sessionById(session.id) should be(session)

  test("sessionStateOf"):
    new App:
      val session = createSession()
      val sj      = serverJson()
      serverSessionsService.setSessionJsonState(session, sj)
      serverSessionsService.sessionStateOf(session).serverJson should be(sj)

  test("removeSession"):
    new App:
      val session = createSession()
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.removeSession(session)
      an[IllegalArgumentException] should be thrownBy:
        serverSessionsService.sessionById(session.id)

  test("removeSession notifies listeners"):
    new App:
      val session        = createSession()
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
      val session = createSession(SessionOptions.LeaveOpenWhenTerminated)
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.terminateSession(session)
      serverSessionsService.sessionById(session.id).isOpen should be(false)

  test("terminateSession doesn't terminate a session that should always be open"):
    new App:
      val session = createSession(SessionOptions(alwaysOpen = true))
      serverSessionsService.setSessionJsonState(session, serverJson())
      an[IllegalArgumentException] should be thrownBy:
        serverSessionsService.terminateSession(session)

  test("terminateSession removes session if marked to be deleted when terminated"):
    new App:
      val session = createSession(SessionOptions(closeTabWhenTerminated = true))
      serverSessionsService.setSessionJsonState(session, serverJson())
      serverSessionsService.terminateSession(session)
      serverSessionsService.allSessions should be(Nil)

  test("terminateSession notifies session listeners"):
    new App:
      val session     = createSession()
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
      val session        = createSession()
      serverSessionsService.setSessionJsonState(session, serverJson())
      var listenerCalled = 0
      serverSessionsService.notifyMeWhenSessionsChange: sessions =>
        listenerCalled match
          case 0 => sessions should be(Seq(session))
          case 1 => sessions should be(Seq(session.close))
          case 2 => sessions should be(Nil)
        listenerCalled += 1
        true
      serverSessionsService.terminateSession(session)
      listenerCalled should be(3)

  test("createSession notifies listeners"):
    new App:
      var listenerCalled = 0
      serverSessionsService.notifyMeWhenSessionsChange: sessions =>
        listenerCalled match
          case 0 => sessions should be(Nil)
          case 1 => sessions.size should be(1)
        listenerCalled += 1
        true

      createSession()
      listenerCalled should be(2)

  test("changeSessionJsonState changes session's state"):
    ???
//    new App:
//      val session = createSession()
//      val sj1     = serverJson(elements = Map("e1" -> Json.fromString("e1v")))
//      serverSessionsService.setSessionJsonState(session, sj1)
//      val sj2     = serverJson(elements = Map("e2" -> Json.fromString("e2v")))
//      serverSessionsService.changeSessionJsonState(session, sj2)
//      serverSessionsService.sessionStateOf(session).serverJson should be(
//        sj1.include(sj2)
//      )

  test("changeSessionJsonState notifies listeners"):
    ???
//    new App:
//      val session = createSession()
//      val sj1     = serverJson(elements = Map("e1" -> Json.fromString("e1v")))
//      serverSessionsService.setSessionJsonState(session, sj1)
//      val sj2     = serverJson(elements = Map("e2" -> Json.fromString("e2v")))
//      var called  = 0
//      serverSessionsService.notifyMeWhenSessionChanges: (s, sessionState, sjOption) =>
//        called match
//          case 0 =>
//            s should be(session)
//            sjOption should be(None)
//          case 1 =>
//            s should be(session)
//            sjOption should be(Some(sj2))
//
//        called += 1
//        true
//      serverSessionsService.changeSessionJsonState(session, sj2)
//      called should be(2)

  test("triggerUiEvent notifies listeners for clicks"):
    new App:
      val session = createSession()
      var called  = false
      serverSessionsService.notifyMeOnSessionEvents(session): e =>
        called = true
        e should be(OnClick("key1"))
        true

      serverSessionsService.triggerUiEvent(json.OnClick(session.id, "key1"))
      called should be(true)

  test("triggerUiEvent notifies listeners for change"):
    new App:
      val session = createSession()
      var called  = false
      serverSessionsService.notifyMeOnSessionEvents(session): e =>
        called = true
        e should be(OnChange("key1", "newvalue"))
        true

      serverSessionsService.triggerUiEvent(json.OnChange(session.id, "key1", "newvalue"))
      called should be(true)

  class App:
    val serverSessionsService                                            = new ServerSessionsService
    def createSession(options: SessionOptions = SessionOptions.Defaults) = serverSessionsService.createSession("test", "Test", options)
