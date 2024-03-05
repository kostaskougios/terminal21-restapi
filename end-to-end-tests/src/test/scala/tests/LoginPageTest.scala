//package tests
//
//import org.scalatest.funsuite.AnyFunSuiteLike
//import org.scalatest.matchers.should.Matchers.*
//import org.terminal21.client.components.*
//import org.terminal21.client.{ConnectedSession, ConnectedSessionMock}
//import org.terminal21.model.CommandEvent
//
//class LoginPageTest extends AnyFunSuiteLike:
//
//  class App:
//    given session: ConnectedSession   = ConnectedSessionMock.newConnectedSessionMock
//    val login                         = LoginForm()
//    val page                          = new LoginPage
//    def allComponents: Seq[UiElement] = page.components.flatMap(_.flat)
//
//  test("renders email input"):
//    new App:
//      allComponents should contain(page.emailInput)
//
//  test("renders password input"):
//    new App:
//      allComponents should contain(page.passwordInput)
//
//  test("renders submit button"):
//    new App:
//      allComponents should contain(page.submitButton)
//
//  test("user submits validated data"):
//    new App:
//      val eventsIt = page.controller.render().handledEventsIterator // get the iterator before we fire the events, otherwise the iterator will be empty
//      session.fireEvents(
//        CommandEvent.onChange(page.emailInput, "an@email.com"),
//        CommandEvent.onChange(page.passwordInput, "secret"),
//        CommandEvent.onClick(page.submitButton),
//        CommandEvent.sessionClosed // every test should close the session so that the iterator doesn't block if converted to a list.
//      )
//
//      eventsIt.lastOption.map(_.model) should be(Some(LoginForm("an@email.com", "secret", true)))
