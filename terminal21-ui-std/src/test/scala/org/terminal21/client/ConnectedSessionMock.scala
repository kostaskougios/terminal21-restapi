package org.terminal21.client

import org.mockito.Mockito.mock
import org.terminal21.client.components.{StdElementEncoding, UiElementEncoding}
import org.terminal21.model.CommonModelBuilders.session
import org.terminal21.ui.std.SessionsService

object ConnectedSessionMock:
  val encoding = new UiElementEncoding(Seq(StdElementEncoding))
  val encoder  = ConnectedSessionMock.encoding.uiElementEncoder

  def newConnectedSessionAndSessionServiceMock: (SessionsService, ConnectedSession) =
    val sessionsService = mock(classOf[SessionsService])
    (sessionsService, new ConnectedSession(session(), encoding, "test", sessionsService, () => ()))

  def newConnectedSessionMock: ConnectedSession = newConnectedSessionAndSessionServiceMock._2
