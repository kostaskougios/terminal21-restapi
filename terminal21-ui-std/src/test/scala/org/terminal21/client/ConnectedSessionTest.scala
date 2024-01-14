package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.chakra.Editable
import org.terminal21.model.OnChange

class ConnectedSessionTest extends AnyFunSuiteLike:
  test("default event handlers are invoked before user handlers"):
    given connectedSession: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val editable                             = Editable()
    editable.onChange: newValue =>
      editable.value should be(newValue)

    connectedSession.add(editable)
    connectedSession.fireEvent(OnChange(editable.key, "new value"))
