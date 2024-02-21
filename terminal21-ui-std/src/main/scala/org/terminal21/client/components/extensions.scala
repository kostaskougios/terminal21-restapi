package org.terminal21.client.components

import org.terminal21.client.ConnectedSession

extension (s: Seq[UiElement])
  def render()(using session: ConnectedSession): Unit =
    session.render(s*)

  def renderChanges()(using session: ConnectedSession): Unit =
    session.renderChanges(s*)
