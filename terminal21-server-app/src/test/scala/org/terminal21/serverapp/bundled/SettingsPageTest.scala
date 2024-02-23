package org.terminal21.serverapp.bundled

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.{*, given}
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.model.CommandEvent

class SettingsPageTest extends AnyFunSuiteLike:
  class App:
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val page                        = new SettingsPage

  test("Should render the ThemeToggle component"):
    new App:
      page.components should contain(page.themeToggle)
