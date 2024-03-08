package org.terminal21.serverapp

import functions.fibers.FiberExecutor
import org.mockito.Mockito
import org.mockito.Mockito.{verify, when}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar.*
import org.terminal21.model.{CommonModelBuilders, SessionOptions}
import org.terminal21.model.CommonModelBuilders.session
import org.terminal21.server.service.ServerSessionsService

class ServerSideSessionsTest extends AnyFunSuiteLike with BeforeAndAfterAll:
  val executor                            = FiberExecutor()
  override protected def afterAll(): Unit = executor.shutdown()

  test("creates session"):
    new App:
      val s = session()
      when(sessionsService.createSession(s.id, s.name, SessionOptions.Defaults)).thenReturn(s)
      serverSideSessions
        .withNewSession(s.id, s.name)
        .connect: session =>
          session.leaveSessionOpenAfterExiting()

      verify(sessionsService).createSession(s.id, s.name, SessionOptions.Defaults)

  test("terminates session before exiting"):
    new App:
      val s = session()
      when(sessionsService.createSession(s.id, s.name, SessionOptions.Defaults)).thenReturn(s)
      serverSideSessions
        .withNewSession(s.id, s.name)
        .connect: _ =>
          ()

      verify(sessionsService).terminateSession(s)

  test("registers to receive events"):
    new App:
      val s = session()
      when(sessionsService.createSession(s.id, s.name, SessionOptions.Defaults)).thenReturn(s)
      serverSideSessions
        .withNewSession(s.id, s.name)
        .connect: _ =>
          ()

      verify(sessionsService).notifyMeOnSessionEvents(s)

  class App:
    val sessionsService    = mock[ServerSessionsService]
    val serverSideSessions = new ServerSideSessions(sessionsService, executor)
