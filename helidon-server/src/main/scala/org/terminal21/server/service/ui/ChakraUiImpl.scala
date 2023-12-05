package org.terminal21.server.service.ui

import org.terminal21.server.json.Chakra
import org.terminal21.server.service.ServerSessionsService
import org.terminal21.ui.std.ChakraUi
import org.terminal21.ui.std.json.chakra.ChakraElement
import org.terminal21.ui.std.model.Session

class ChakraUiImpl(sessionsService: ServerSessionsService) extends ChakraUi:
  override def element(session: Session, element: ChakraElement): Unit =
    sessionsService.modifySessionState(session): state =>
      state.addResponse(Chakra(element))

trait ChakraUiImplBeans:
  def sessionsService: ServerSessionsService
  lazy val chakraUi = new ChakraUiImpl(sessionsService)
