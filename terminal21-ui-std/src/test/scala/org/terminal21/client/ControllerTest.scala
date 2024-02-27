package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, Button, Checkbox}
import org.terminal21.client.components.std.Input
import org.terminal21.collections.SEList
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

class ControllerTest extends AnyFunSuiteLike:
  val button         = Button()
  val buttonClick    = OnClick(button.key)
  val input          = Input()
  val inputChange    = OnChange(input.key, "new-value")
  val checkbox       = Checkbox()
  val checkBoxChange = OnChange(checkbox.key, "true")

  def newController[M](
      initialModel: Model[M],
      events: => Seq[CommandEvent],
      components: Seq[UiElement],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller[M] =
    val seList = SEList[CommandEvent]()
    val it     = seList.iterator
    events.foreach(e => seList.add(e))
    seList.add(CommandEvent.sessionClosed)
    new Controller(it, _ => (), renderChanges, components, initialModel)

  test("onEvent is called"):
    val model = Model(0)
    newController(model, Seq(buttonClick), Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    val model = Model(0)
    newController(model, Seq(inputChange), Seq(input))
      .onEvent: event =>
        import event.*
        if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
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
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  test("onEvent is called for change/boolean"):
    val model = Model(0)
    newController(model, Seq(checkBoxChange), Seq(checkbox))
      .onEvent: event =>
        import event.*
        if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
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
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 5))

  test("onEvent when no partial function matches ClientEvent"):
    val model = Model(0)
    newController(model, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClickEvent(`checkbox`, handled) =>
          handled.withModel(5).terminate
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
    ).handledEventsIterator
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
    ).handledEventsIterator
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
    ).handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 100))

  test("terminate is obeyed and latest model state is iterated"):
    val model = Model(0)
    newController(model, Seq(buttonClick, buttonClick, buttonClick), Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s

    newController(Model(0), Seq(buttonClick), Seq(button), renderer)
      .onEvent: event =>
        event.handled.withModel(event.model + 1).withRenderChanges(button.withText("changed")).terminate
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

    rendered should be(Seq(button.withText("changed")))

  test("changes are rendered once"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s

    val model   = Model(0)
    val handled = newController(
      model,
      Seq(buttonClick, checkBoxChange),
      Seq(
        button.onClick(using model): event =>
          event.handled.withRenderChanges(button.withText("changed")),
        checkbox
      ),
      renderer
    ).handledEventsIterator.toList

    handled(1).renderChanges should be(List(button.withText("changed")))
    handled(2).renderChanges should be(Nil)

  test("timed changes are rendered"):
    @volatile var rendered                = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    newController(Model(0), Seq(buttonClick), Seq(button), renderer)
      .onEvent: event =>
        event.handled.withModel(1).withTimedRenderChanges(TimedRenderChanges(10, button.withText("changed"))).terminate
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))
    Thread.sleep(15)
    rendered should be(Seq(button.withText("changed")))

  test("timed changes are visible"):
    val model = Model(0)
    newController(
      model,
      Seq(buttonClick),
      Seq(
        button.onClick(using model): event =>
          event.handled.withTimedRenderChanges(TimedRenderChanges(10, button.withText("changed"))).terminate
      )
    ).handledEventsIterator.toList(1).current(button) should be(button.withText("changed"))

  test("timed changes event handlers are called"):
    val model = Model(0)
    val c     = checkbox.onChange(using model): event =>
      event.handled.withModel(2)
    newController(
      model,
      Seq(buttonClick, checkBoxChange),
      Seq(
        button.onClick(using model): event =>
          event.handled.withTimedRenderChanges(TimedRenderChanges(10, c))
      )
    ).handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0, 2))

  test("current value for OnChange"):
    val model = Model(0)
    newController(
      model,
      Seq(inputChange),
      Seq(
        input.onChange(using model): event =>
          import event.*
          handled.withModel(if input.current.value == "new-value" then 100 else -1).terminate
      )
    ).handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 100))

  test("current value for OnChange/boolean"):
    val model = Model(0)
    newController(
      model,
      Seq(checkBoxChange),
      Seq(
        checkbox.onChange(using model): event =>
          import event.*
          handled.withModel(if checkbox.current.checked then 100 else -1).terminate
      )
    ).handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 100))

  test("newly rendered elements are visible"):
    val model         = Model(0)
    lazy val box: Box = Box().withChildren(
      button.onClick(using model): event =>
        event.handled.withRenderChanges(box.withChildren(button, checkbox))
    )

    val handledEvents = newController(model, Seq(buttonClick), Seq(box)).handledEventsIterator.toList
    handledEvents(1).componentsByKey(checkbox.key) should be(checkbox)

  test("newly rendered elements event handlers are invoked"):
    val model          = Model(0)
    lazy val b: Button = button.onClick(using model): event =>
      event.handled
        .withModel(1)
        .withRenderChanges(
          box.withChildren(
            b,
            checkbox.onChange(using model): event =>
              event.handled.withModel(2)
          )
        )

    lazy val box: Box = Box().withChildren(b)

    newController(model, Seq(buttonClick, checkBoxChange), Seq(box)).handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1, 2))

  test("RenderChangesEvent renders changes"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s

    newController(Model(0), Seq(RenderChangesEvent(Seq(button.withText("changed")))), Seq(button), renderer).handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))
    rendered should be(Seq(button.withText("changed")))
