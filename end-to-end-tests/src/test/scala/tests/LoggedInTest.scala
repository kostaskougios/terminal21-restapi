package tests

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
import org.terminal21.model.CommandEvent

class LoggedInTest extends AnyFunSuiteLike:
  class App:
    val login                       = Login("my@email.com", "secret")
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val form                        = new LoggedIn(login)
    def allComponents               = form.components.flatMap(_.flat)

  test("renders email details"):
    new App:
      allComponents should contain(form.emailDetails)

  test("renders password details"):
    new App:
      allComponents should contain(form.passwordDetails)

  test("yes clicked"):
    new App:
      val eventsIt = form.controller.render().handledEventsIterator
      session.fireEvents(CommandEvent.onClick(form.yesButton), CommandEvent.sessionClosed)
      eventsIt.lastOption.map(_.model) should be(Some(true))

  test("no clicked"):
    new App:
      val eventsIt = form.controller.render().handledEventsIterator
      session.fireEvents(CommandEvent.onClick(form.noButton), CommandEvent.sessionClosed)
      eventsIt.lastOption.map(_.model) should be(Some(false))
