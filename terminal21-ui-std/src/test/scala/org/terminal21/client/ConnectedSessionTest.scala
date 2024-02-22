package org.terminal21.client

import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.ConnectedSessionMock.encoder
import org.terminal21.client.components.chakra.{Button, Checkbox, Editable, Input}
import org.terminal21.client.components.std.{Paragraph, Span}
import org.terminal21.client.model.UiEvent
import org.terminal21.model.{CommandEvent, OnChange}
import org.terminal21.ui.std.ServerJson

import java.util.concurrent.atomic.AtomicBoolean

class ConnectedSessionTest extends AnyFunSuiteLike:

  test("global event iterator"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val editable                             = Editable()
    editable.render()
    val it                                   = connectedSession.eventIterator
    val event1                               = OnChange(editable.key, "v1")
    val event2                               = OnChange(editable.key, "v2")
    connectedSession.fireEvent(event1)
    connectedSession.fireEvent(event2)
    connectedSession.clear()
    it.toList should be(
      List(
        UiEvent(event1, editable.copy(valueReceived = Some("v1"))),
        UiEvent(event2, editable.copy(valueReceived = Some("v2")))
      )
    )

  test("click events are processed by onClick handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    var clicked                              = false
    val button                               = Button().onClick: () =>
      clicked = true
    connectedSession.render(button)
    connectedSession.fireEvent(CommandEvent.onClick(button))
    clicked should be(true)

  test("change events are processed by onChange handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    var value                                = ""
    val input                                = Input().onChange: newValue =>
      value = newValue
    connectedSession.render(input)
    connectedSession.fireEvent(CommandEvent.onChange(input, "new-value"))
    value should be("new-value")

  test("change boolean events are processed by onChange handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    var value                                = false
    val checkbox                             = Checkbox().onChange: newValue =>
      value = newValue
    connectedSession.render(checkbox)
    connectedSession.fireEvent(CommandEvent.onChange(checkbox, true))
    value should be(true)

  test("default event handlers are invoked before user handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val editableI                            = Editable()
    val editable                             = editableI.onChange: newValue =>
      editableI.current.value should be(newValue)

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

  test("renderChanges updates current version of component when component deeply nested"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock

    val span1 = Span(text = "span1")
    val p1    = Paragraph(text = "p1").withChildren(span1)
    connectedSession.render(p1)
    connectedSession.renderChanges(p1.withChildren(span1.withText("span-text-changed")))
    span1.current.text should be("span-text-changed")
