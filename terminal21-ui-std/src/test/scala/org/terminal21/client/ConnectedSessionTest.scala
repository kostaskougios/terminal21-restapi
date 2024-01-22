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

  test("default event handlers are invoked before user handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val editable                             = Editable()
    editable.onChange: newValue =>
      editable.current.value should be(newValue)

    connectedSession.render(editable)
    connectedSession.fireEvent(OnChange(editable.key, "new value"))

  test("to server json"):
    val (sessionService, connectedSession) = ConnectedSessionMock.newConnectedSessionAndSessionServiceMock

    val p1    = Paragraph(text = "p1")
    val span1 = Span(text = "span1")
    connectedSession.render(p1.withChildren(span1))
    connectedSession.render()
    verify(sessionService).setSessionJsonState(
      connectedSession.session,
      ServerJson(
        Seq(p1.key),
        Map(p1.key -> encoder(p1.withChildren()), span1.key -> encoder(span1)),
        Map(p1.key -> Seq(span1.key), span1.key             -> Nil)
      )
    )

  test("renderChanges changes state on server"):
    val (sessionService, connectedSession) = ConnectedSessionMock.newConnectedSessionAndSessionServiceMock

    val p1    = Paragraph(text = "p1")
    val span1 = Span(text = "span1")
    connectedSession.render(p1)
    connectedSession.renderChanges(p1.withChildren(span1))
    verify(sessionService).changeSessionJsonState(
      connectedSession.session,
      ServerJson(
        Seq(p1.key),
        Map(p1.key -> encoder(p1.withChildren()), span1.key -> encoder(span1)),
        Map(p1.key -> Seq(span1.key), span1.key             -> Nil)
      )
    )

  test("renderChanges updates current version of component"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock

    val p1    = Paragraph(text = "p1")
    val span1 = Span(text = "span1")
    connectedSession.render(p1)
    connectedSession.renderChanges(p1.withChildren(span1))
    p1.current.children should be(Seq(span1))
