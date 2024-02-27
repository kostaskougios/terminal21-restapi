package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, Button, ChakraElement, Checkbox}
import org.terminal21.client.components.std.Input
import org.terminal21.collections.SEList
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

class ControllerTest extends AnyFunSuiteLike:
  val button             = Button()
  val buttonClick        = OnClick(button.key)
  val input              = Input()
  val inputChange        = OnChange(input.key, "new-value")
  val checkbox           = Checkbox()
  val checkBoxChange     = OnChange(checkbox.key, "true")
  given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock

  def newController[M](
      initialModel: Model[M],
      events: => Seq[CommandEvent],
      modelComponents: M => Seq[UiElement],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller[M] =
    val seList = SEList[CommandEvent]()
    val it     = seList.iterator
    events.foreach(e => seList.add(e))
    seList.add(CommandEvent.sessionClosed)
    new Controller(it, renderChanges, modelComponents, initialModel, Nil)

  test("onEvent is called"):
    val model = Model(0)
    newController(model, Seq(buttonClick), _ => Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    val model = Model(0)
    newController(model, Seq(inputChange), _ => Seq(input))
      .onEvent: event =>
        import event.*
        if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matched for change"):
    val model = Model(0)
    newController(model, Seq(inputChange), _ => Seq(input))
      .onEvent:
        case event: ControllerClickEvent[_] =>
          import event.*
          handled.withModel(5)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  test("onEvent is called for change/boolean"):
    val model = Model(0)
    newController(model, Seq(checkBoxChange), _ => Seq(checkbox))
      .onEvent: event =>
        import event.*
        if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matches for change/boolean"):
    val model = Model(0)
    newController(model, Seq(checkBoxChange), _ => Seq(checkbox))
      .onEvent:
        case event: ControllerClickEvent[_] =>
          import event.*
          handled.withModel(5)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  case class TestClientEvent(i: Int) extends ClientEvent

  test("onEvent is called for ClientEvent"):
    val model = Model(0)
    newController(model, Seq(TestClientEvent(5)), _ => Seq(button))
      .onEvent:
        case ControllerClientEvent(handled, event: TestClientEvent) =>
          import event.*
          handled.withModel(event.i).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 5))

  test("onEvent when no partial function matches ClientEvent"):
    val model = Model(0)
    newController(model, Seq(TestClientEvent(5)), _ => Seq(button))
      .onEvent:
        case ControllerClickEvent(`checkbox`, handled) =>
          handled.withModel(5).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  test("onClick is called"):
    given model: Model[Int] = Model(0)
    newController(
      model,
      Seq(buttonClick),
      _ =>
        Seq(
          button.onClick: event =>
            event.handled.withModel(100).terminate
        )
    ).render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 100))

  test("onChange is called"):
    given model: Model[Int] = Model(0)
    newController(
      model,
      Seq(inputChange),
      _ =>
        Seq(
          input.onChange: event =>
            event.handled.withModel(100).terminate
        )
    ).render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 100))

  test("onChange/boolean is called"):
    given model: Model[Int] = Model(0)
    newController(
      model,
      Seq(checkBoxChange),
      _ =>
        Seq(
          checkbox.onChange: event =>
            event.handled.withModel(100).terminate
        )
    ).render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 100))

  test("terminate is obeyed and latest model state is iterated"):
    val model = Model(0)
    newController(model, Seq(buttonClick, buttonClick, buttonClick), _ => Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    def components(m: Int)                = Seq(
      m match
        case 0 => button
        case 1 => button.withText("changed")
    )

    newController(Model(0), Seq(buttonClick), components, renderer)
      .onEvent: event =>
        event.handled.withModel(event.model + 1).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

    rendered should be(Seq(button.withText("changed")))
