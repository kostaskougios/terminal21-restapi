package tests

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.*
import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
import org.terminal21.model.CommandEvent

class LoginFormTest extends AnyFunSuiteLike:

  class App:
    given session: ConnectedSession = ConnectedSessionMock.newConnectedSessionMock
    val form                        = new LoginForm(using session)
    def allComponents               = form.components.flatMap(_.flat)

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
      val eventsIt = form.controller.render().handledEventsIterator // get the iterator before we fire the events, otherwise the iterator will be empty
      session.fireEvents(
        CommandEvent.onChange(form.emailInput, "an@email.com"),
        CommandEvent.onChange(form.passwordInput, "secret"),
        CommandEvent.onClick(form.submitButton),
        CommandEvent.sessionClosed // every test should close the session so that the iterator doesn't block if converted to a list.
      )

      eventsIt.lastOption.map(_.model) should be(Some(Login("an@email.com", "secret")))

  test("user submits invalid email"):
    new App:
      val eventsIt   = form.controller.render().handledEventsIterator // get the iterator that iterates Handled instances so that we can assert on renderChanges
      session.fireEvents(
        CommandEvent.onChange(form.emailInput, "invalid-email.com"),
        CommandEvent.onClick(form.submitButton),
        CommandEvent.sessionClosed
      )
      val allHandled = eventsIt.toList
      // the event processing shouldn't have terminated because of the email error
      allHandled.exists(_.shouldTerminate) should be(false)
      // the email right addon should have rendered with the notOkIcon when the user typed the incorrect email
      allHandled(1).renderChanges should be(Seq(form.emailRightAddon.withChildren(form.notOkIcon)))
      // An error message in the errorsBox should be displayed when the user clicked on the submit
      allHandled(2).renderChanges should be(Seq(form.errorsBox.withChildren(form.errorMsgInvalidEmail)))
