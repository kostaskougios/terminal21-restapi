package org.terminal21.server.service.ui

import org.terminal21.server.json.Std
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.ui.std.StdUi
import org.terminal21.ui.std.json.StdElement
import org.terminal21.ui.std.model.Session

class StdUiImpl(sessionsService: ServerSessionsService) extends StdUi:
  override def element(session: Session, element: StdElement): Unit =
    sessionsService.modifySessionState(session): state =>
      state.addResponse(Std(element))

trait StdUiImplBeans:
  def sessionsService: ServerSessionsService
  lazy val stdUi = new StdUiImpl(sessionsService)
