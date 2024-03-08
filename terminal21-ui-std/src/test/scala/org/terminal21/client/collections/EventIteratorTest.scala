package org.terminal21.client.collections

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.ConnectedSessionMock
import org.terminal21.model.{CommandEvent, SessionClosed}

class EventIteratorTest extends AnyFunSuiteLike:
  test("works as normal iterator"):
    EventIterator(1, 2, 3).toList should be(List(1, 2, 3))

  test("works as normal iterator when empty"):
    EventIterator().toList should be(Nil)

  test("lastOption when available"):
    EventIterator(1, 2, 3).lastOption should be(Some(3))

  test("lastOption when not available"):
    EventIterator().lastOption should be(None)

  test("lastOptionOrNoneIfSessionClosed when session open"):
    val session = ConnectedSessionMock.newConnectedSessionMock
    EventIterator(1, 2).lastOptionOrNoneIfSessionClosed(using session) should be(Some(2))

  test("lastOptionOrNoneIfSessionClosed when session closed"):
    val session = ConnectedSessionMock.newConnectedSessionMock
    session.fireEvent(CommandEvent.sessionClosed)
    EventIterator(1, 2).lastOptionOrNoneIfSessionClosed(using session) should be(None)
