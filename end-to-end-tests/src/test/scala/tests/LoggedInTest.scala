package tests

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.*
import org.terminal21.model.CommandEvent

class LoggedInTest extends AnyFunSuiteLike:
  class App:
    val login                       = LoginForm()
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val form                        = new LoggedIn(login)
    def allComponents               = form.components(false, Events.Empty).view.flatMap(_.flat)

  test("renders email details"):
    new App:
      allComponents should contain(form.emailDetails)

  test("renders password details"):
    new App:
      allComponents should contain(form.passwordDetails)

  test("yes clicked"):
    new App:
      val eventsIt = form.controller.render(false).iterator
      session.fireEvents(CommandEvent.onClick(form.yesButton), CommandEvent.sessionClosed)
      eventsIt.lastOption.map(_.model) should be(Some(true))

  test("no clicked"):
    new App:
      val eventsIt = form.controller.render(false).iterator
      session.fireEvents(CommandEvent.onClick(form.noButton), CommandEvent.sessionClosed)
      eventsIt.lastOption.map(_.model) should be(Some(false))
