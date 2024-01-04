package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.UiComponent
import org.terminal21.client.components.chakra.{Box, Editable}
import org.terminal21.model.OnChange

class ConnectedSessionTest extends AnyFunSuiteLike:
  test("default event handlers are invoked before user handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val editable                             = Editable()
    editable.onChange: newValue =>
      editable.value should be(newValue)

    connectedSession.add(editable)
    connectedSession.fireEvent(OnChange(editable.key, "new value"))

  test("components have their children added"):
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock

    val box       = Box(key = "1")
    val component = new UiComponent {
      var children     = Seq(box)
      override def key = "2"
    }
    session.add(component)
    session.allElements should be(Seq(box))
