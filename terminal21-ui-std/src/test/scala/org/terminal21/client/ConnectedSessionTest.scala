package org.terminal21.client

import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.ConnectedSessionMock.encoder
import org.terminal21.client.components.chakra.Editable
import org.terminal21.client.components.std.{Paragraph, Span}
import org.terminal21.model.OnChange
import org.terminal21.ui.std.ServerJson

class ConnectedSessionTest extends AnyFunSuiteLike:

  test("event iterator"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val editable                             = Editable(key = "ed")
    val it                                   = connectedSession.eventIterator
    val event1                               = OnChange(editable.key, "v1")
    val event2                               = OnChange(editable.key, "v2")
    connectedSession.fireEvent(event1)
    connectedSession.fireEvent(event2)
    connectedSession.clear()
    it.toList should be(
      List(
        event1,
        event2
      )
    )

  test("to server json"):
    val (sessionService, connectedSession) = ConnectedSessionMock.newConnectedSessionAndSessionServiceMock
    val span1                              = Span("sk", text = "span1")
    val p1                                 = Paragraph("pk", text = "p1").withChildren(span1)
    connectedSession.render(Seq(p1.withChildren(span1)))
    verify(sessionService).setSessionJsonState(
      connectedSession.session,
      ServerJson(
        Seq(encoder(p1).deepDropNullValues)
      )
    )
