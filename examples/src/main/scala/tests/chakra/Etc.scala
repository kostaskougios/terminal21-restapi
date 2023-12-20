package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.Center

object Etc :
  def components(using session: ConnectedSession): Seq[UiElement] =
    Seq(
      Center(text="Center demo")
    )