package org.terminal21.client

import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.chakra.Editable
import org.terminal21.model.CommonModelBuilders.*
import org.terminal21.model.OnChange
import org.terminal21.ui.std.SessionsService

class ConnectedSessionTest extends AnyFunSuiteLike:
  test("default event handlers are invoked before user handlers"):
    val sessionsService                      = mock(classOf[SessionsService])
    given connectedSession: ConnectedSession = new ConnectedSession(session(), "test", sessionsService)
    val editable                             = Editable()
    editable.onChange: newValue =>
      editable.value should be(newValue)

    connectedSession.add(editable)
    connectedSession.fireEvent(OnChange(editable.key, "new value"))
