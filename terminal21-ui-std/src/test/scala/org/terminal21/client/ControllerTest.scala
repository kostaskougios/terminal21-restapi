package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Button, Checkbox}
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
      modelComponents: Seq[UiElement],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller[M] =
    val seList = SEList[CommandEvent]()
    val it     = seList.iterator
    events.foreach(e => seList.add(e))
    seList.add(CommandEvent.sessionClosed)
    new Controller(it, renderChanges, modelComponents, initialModel, Nil)

  test("will throw an exception if there is a duplicate key"):
    an[IllegalArgumentException] should be thrownBy
      newController(Model(0), Seq(buttonClick), Seq(button, button)).render().handledEventsIterator

  test("onEvent is called"):
    val model = Model(0)
    newController(model, Seq(buttonClick), Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    val model = Model(0)
    newController(model, Seq(inputChange), Seq(input))
      .onEvent: event =>
        import event.*
        if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matched for change"):
    val model = Model(0)
    newController(model, Seq(inputChange), Seq(input))
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
    newController(model, Seq(checkBoxChange), Seq(checkbox))
      .onEvent: event =>
        import event.*
        if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matches for change/boolean"):
    val model = Model(0)
    newController(model, Seq(checkBoxChange), Seq(checkbox))
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
    newController(model, Seq(TestClientEvent(5)), Seq(button))
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
    newController(model, Seq(TestClientEvent(5)), Seq(button))
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
    newController(model, Seq(buttonClick, buttonClick, buttonClick), Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    given model: Model[Int]               = Model(0)
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    val but                               = button.onModelChange: (b, m) =>
      b.withText(s"changed $m")

    val handled = newController(model, Seq(buttonClick), Seq(but), renderer)
      .onEvent: event =>
        event.handled.withModel(event.model + 1).terminate
      .render()
      .handledEventsIterator
      .toList

    val expected = Seq(but.withText("changed 1"))
    rendered should be(expected)
    handled.map(_.renderedChanges)(1) should be(expected)

  test("rendered are cleared"):
    given model: Model[Int]               = Model(0)
    var lastRendered                      = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = lastRendered = s
    val but                               = button.onModelChange: (b, m) =>
      if m == 1 then b.withText(s"changed $m") else b

    val handled = newController(model, Seq(buttonClick, checkBoxChange), Seq(but, checkbox), renderer)
      .onEvent: event =>
        val h = event.handled.withModel(event.model + 1)
        if h.model > 1 then h.terminate else h
      .render()
      .handledEventsIterator
      .toList

    lastRendered should be(Nil)
    val rendered = handled.map(_.renderedChanges)
    rendered.head should be(Nil)
    rendered(1) should be(Seq(but.withText("changed 1")))
    rendered(2) should be(Nil)
