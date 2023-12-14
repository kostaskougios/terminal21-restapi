package org.terminal21.client.components

import org.terminal21.client.ConnectedSession

trait UiElement:
  def key: String

  def render()(using session: ConnectedSession): Unit =
    session.add(this)
    session.render()
