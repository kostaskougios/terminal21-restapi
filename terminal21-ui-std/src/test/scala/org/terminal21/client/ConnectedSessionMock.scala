package org.terminal21.client

import org.mockito.Mockito.mock
import org.terminal21.client.components.{StdElementEncoding, UiElementEncoding}
import org.terminal21.model.CommonModelBuilders.session
import org.terminal21.ui.std.SessionsService

object ConnectedSessionMock:
  def newConnectedSessionMock: ConnectedSession =
    val sessionsService = mock(classOf[SessionsService])
    new ConnectedSession(session(), new UiElementEncoding(Seq(StdElementEncoding)), "test", sessionsService, () => ())
