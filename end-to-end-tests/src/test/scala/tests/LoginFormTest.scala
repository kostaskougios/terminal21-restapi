package tests

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock, Controller}
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.model.*
import org.terminal21.client.components.*
import org.terminal21.model.CommandEvent

class LoginFormTest extends AnyFunSuiteLike:
  test("renders email input"):
    new App:
      allComponents should contain(form.emailInput)

  test("renders password input"):
    new App:
      allComponents should contain(form.passwordInput)

  test("renders submit button"):
    new App:
      allComponents should contain(form.submitButton)

  test("user submits validated data"):
    new App:
      form.components.render()
      val eventsIt = form.controller.iterator // get the iterator before we fire the events, otherwise the iterator will be empty
      session.fireEvents(
        CommandEvent.onChange(form.emailInput, "an@email.com"),
        CommandEvent.onChange(form.passwordInput, "secret"),
        CommandEvent.onClick(form.submitButton)
      )

      eventsIt.toList.lastOption should be(Some(Login("an@email.com", "secret")))

  class App:
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val form                        = new LoginForm(using session)
    def allComponents               = form.components.flatMap(_.flat)
