package org.terminal21.client

import org.scalatest.funsuite.AnyFunSuiteLike
import org.terminal21.client.components.chakra.Button
import org.terminal21.client.model.UiEvent
import org.terminal21.model.OnClick
import org.scalatest.matchers.should.Matchers.*

class ControllerTest extends AnyFunSuiteLike:
  val button      = Button()
  val buttonClick = UiEvent(OnClick(button.key), button)

  test("onEvent is called"):
    Controller(0, Iterator(buttonClick))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate else event.handled.withModel(event.model + 1)
      .iterator
      .toList should be(List(0, 1))

  test("onClick is called"):
    Controller(0, Iterator(buttonClick))
      .onClick(button): event =>
        event.handled.withModel(100).terminate
      .iterator
      .toList should be(List(0, 100))

  test("terminate is obeyed and model when terminating is iterated"):
    Controller(0, Iterator(buttonClick, buttonClick, buttonClick))
      .onEvent: event =>
        if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .iterator
      .toList should be(List(0, 1, 2, 100))
