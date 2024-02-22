package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Button, Checkbox}
import org.terminal21.client.components.std.Input
import org.terminal21.model.{CommandEvent, OnChange, OnClick}

class ControllerTest extends AnyFunSuiteLike:
  val button         = Button()
  val buttonClick    = OnClick(button.key)
  val input          = Input()
  val inputChange    = OnChange(input.key, "new-value")
  val checkbox       = Checkbox()
  val checkBoxChange = OnChange(checkbox.key, "true")

  def newController[M](
      initialModel: Model[M],
      eventIterator: => Iterator[CommandEvent],
      components: Seq[UiElement],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller[M] =
    new Controller(eventIterator, renderChanges, components, initialModel, Nil)

  test("onEvent is called"):
    val model = Model(0)
    newController(model, Iterator(buttonClick), Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .eventsIterator
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    val model = Model(0)
    newController(model, Iterator(inputChange), Seq(input))
      .onEvent:
        case event @ ControllerChangeEvent(`input`, handled, "new-value") =>
          if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .eventsIterator
      .toList should be(List(0, 1))

  test("onEvent is called for change/boolean"):
    val model = Model(0)
    newController(model, Iterator(checkBoxChange), Seq(checkbox))
      .onEvent:
        case event @ ControllerChangeBooleanEvent(`checkbox`, handled, true) =>
          if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .eventsIterator
      .toList should be(List(0, 1))

  test("onClick is called"):
    given model: Model[Int] = Model(0)
    newController(
      model,
      Iterator(buttonClick),
      Seq(
        button.onClick: event =>
          event.handled.withModel(100).terminate
      )
    ).eventsIterator.toList should be(List(0, 100))

  test("onChange is called"):
    given model: Model[Int] = Model(0)
    newController(
      model,
      Iterator(inputChange),
      Seq(
        input.onChange: event =>
          event.handled.withModel(100).terminate
      )
    ).eventsIterator.toList should be(List(0, 100))

  test("onChange/boolean is called"):
    given model: Model[Int] = Model(0)
    newController(
      model,
      Iterator(checkBoxChange),
      Seq(
        checkbox.onChange: event =>
          event.handled.withModel(100).terminate
      )
    ).eventsIterator.toList should be(List(0, 100))

  test("terminate is obeyed and latest model state is iterated"):
    val model = Model(0)
    newController(model, Iterator(buttonClick, buttonClick, buttonClick), Seq(button))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .eventsIterator
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s

    newController(Model(0), Iterator(buttonClick), Seq(button), renderer)
      .onEvent: event =>
        event.handled.withModel(event.model + 1).withRenderChanges(button.withText("changed")).terminate
      .eventsIterator
      .toList should be(List(0, 1))

    rendered should be(Seq(button.withText("changed")))

  test("timed changes are rendered"):
    @volatile var rendered                = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    newController(Model(0), Iterator(buttonClick), Seq(button), renderer)
      .onEvent: event =>
        event.handled.withModel(event.model + 1).withTimedRenderChanges(TimedRenderChanges(10, button.withText("changed"))).terminate
      .eventsIterator
      .toList should be(List(0, 1))
    Thread.sleep(15)
    rendered should be(Seq(button.withText("changed")))
