//package org.terminal21.serverapp.bundled
//
//import org.scalatest.funsuite.AnyFunSuiteLike
//import org.scalatestplus.mockito.MockitoSugar.mock
//import org.terminal21.client.components.chakra.{Button, CheckIcon, NotAllowedIcon, Text}
//import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
//import org.terminal21.model.CommonModelBuilders.session
//import org.terminal21.model.{CommandEvent, CommonModelBuilders, Session}
//import org.terminal21.server.service.ServerSessionsService
//import org.terminal21.serverapp.ServerSideSessions
//import org.terminal21.client.given
//import org.scalatest.matchers.should.Matchers.*
//
//class ServerStatusPageTest extends AnyFunSuiteLike:
//  class App:
//    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
//    val sessionsService                      = mock[ServerSessionsService]
//    val serverSideSessions                   = mock[ServerSideSessions]
//    val page                                 = new ServerStatusPage(serverSideSessions, sessionsService)
//
//  test("Close button for a session"):
//    new App:
//      page
//        .sessionsTable(Seq(session()))
//        .flat
//        .collectFirst:
//          case b: Button if b.text == "Close" => b
//        .isEmpty should be(false)
//
//  test("View state button for a session"):
//    new App:
//      page
//        .sessionsTable(Seq(session()))
//        .flat
//        .collectFirst:
//          case b: Button if b.text == "View State" => b
//        .isEmpty should be(false)
//
//  test("When session is open, a CheckIcon is displayed"):
//    new App:
//      page
//        .sessionsTable(Seq(session()))
//        .flat
//        .collectFirst:
//          case i: CheckIcon => i
//        .isEmpty should be(false)
//
//  test("When session is closed, a NotAllowedIcon is displayed"):
//    new App:
//      page
//        .sessionsTable(Seq(session(isOpen = false)))
//        .flat
//        .collectFirst:
//          case i: NotAllowedIcon => i
//        .isEmpty should be(false)
//
//  test("sessions are rendered when Ticker event is fired"):
//    new App:
//      var times         = 0
//      def sessions      =
//        times += 1
//        times match
//          case 1 => Seq(session(id = "s1", name = "session 1")) // this is initially rendered
//          case 2 => Seq(session(id = "s2", name = "session 2")) // this is a change
//          case 3 => Seq(session(id = "s3", name = "session 3")) // this is also a change
//      val it            = page.controller(Runtime.getRuntime, sessions).render().handledEventsIterator
//      connectedSession.fireEvents(page.Ticker, page.Ticker, CommandEvent.sessionClosed)
//      val handledEvents = it.toList
//      handledEvents.head.renderChanges should be(Nil)
//      handledEvents(1).renderChanges
//        .flatMap(_.flat)
//        .collectFirst:
//          case t: Text if t.text == "session 2" => t
//        .size should be(1)
//      handledEvents(2).renderChanges
//        .flatMap(_.flat)
//        .collectFirst:
//          case t: Text if t.text == "session 3" => t
//        .size should be(1)
