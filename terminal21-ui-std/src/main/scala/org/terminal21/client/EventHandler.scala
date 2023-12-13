package org.terminal21.client

trait EventHandler

trait OnClickEventHandler extends EventHandler:
  def onClick(): Unit
