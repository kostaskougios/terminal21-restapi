package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Button, Checkbox}
import org.terminal21.client.components.std.Input
import org.terminal21.client.model.UiEvent
import org.terminal21.model.{OnChange, OnClick}

class ControllerTest extends AnyFunSuiteLike:
  val button         = Button()
  val buttonClick    = UiEvent(OnClick(button.key), button)
  val input          = Input()
  val inputChange    = UiEvent(OnChange(input.key, "new-value"), input)
  val checkbox       = Checkbox()
  val checkBoxChange = UiEvent(OnChange(checkbox.key, "true"), checkbox)

  test("onEvent is called"):
    Controller(0, Iterator(buttonClick))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .iterator
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    Controller(0, Iterator(inputChange))
      .onEvent:
        case event @ ControllerChangeEvent(`input`, 0, "new-value") =>
          if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .iterator
      .toList should be(List(0, 1))

  test("onEvent is called for change/boolean"):
    Controller(0, Iterator(checkBoxChange))
      .onEvent:
        case event @ ControllerChangeBooleanEvent(`checkbox`, 0, true) =>
          if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .iterator
      .toList should be(List(0, 1))

  test("onClick is called"):
    Controller(0, Iterator(buttonClick))
      .onClick(button): event =>
        event.handled.withModel(100).terminate
      .iterator
      .toList should be(List(0, 100))

  test("onChange is called"):
    Controller(0, Iterator(inputChange))
      .onChange(input): event =>
        event.handled.withModel(100).terminate
      .iterator
      .toList should be(List(0, 100))

  test("onChange/boolean is called"):
    Controller(0, Iterator(checkBoxChange))
      .onChange(checkbox): event =>
        event.handled.withModel(100).terminate
      .iterator
      .toList should be(List(0, 100))

  test("terminate is obeyed and latest model state is iterated"):
    Controller(0, Iterator(buttonClick, buttonClick, buttonClick))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .iterator
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s

    Controller(0, Iterator(buttonClick), renderer)
      .onEvent: event =>
        event.handled.withModel(event.model + 1).withRenderChanges(button.withText("changed")).terminate
      .iterator
      .toList should be(List(0, 1))

    rendered should be(Seq(button.withText("changed")))
