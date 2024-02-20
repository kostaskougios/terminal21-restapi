package tests

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.*
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
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
        CommandEvent.onClick(form.submitButton),
        CommandEvent.sessionClosed
      )

      eventsIt.toList.lastOption should be(Some(Login("an@email.com", "secret")))

  test("user submits invalid email"):
    new App:
      form.components.render()
      val eventsIt   = form.controller.handledIterator // get the iterator before we fire the events, otherwise the iterator will be empty
      session.fireEvents(
        CommandEvent.onChange(form.emailInput, "anemail.com"),
        CommandEvent.onClick(form.submitButton),
        CommandEvent.sessionClosed
      )
      val allHandled = eventsIt.toList
      // the form shouldn't have terminated because of the email error
      allHandled.exists(_.shouldTerminate) should be(false)
      // the email right addon should have rendered with the notOkIcon
      allHandled.flatMap(_.renderChanges) should be(Seq(form.emailRightAddon.withChildren(form.notOkIcon)))

  class App:
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val form                        = new LoginForm(using session)
    def allComponents               = form.components.flatMap(_.flat)
