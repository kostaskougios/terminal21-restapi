package org.terminal21.client

import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.mockito.MockitoSugar.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Button, Checkbox, QuickTable}
import org.terminal21.client.components.std.Input
import org.terminal21.collections.SEList
import org.terminal21.model.{ClientEvent, CommandEvent, OnChange, OnClick}

import java.util.concurrent.atomic.AtomicBoolean

class ControllerTest extends AnyFunSuiteLike:
  val button             = Button()
  val buttonClick        = OnClick(button.key)
  val input              = Input()
  val inputChange        = OnChange(input.key, "new-value")
  val checkbox           = Checkbox()
  val checkBoxChange     = OnChange(checkbox.key, "true")
  given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock

  val intModel    = Model[Int]("int-model")
  val stringModel = Model[String]("string-model")

  def newController[M](
      initialModel: Model[M],
      initialValue: M,
      events: => Seq[CommandEvent],
      modelComponents: Seq[UiElement],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller[M] =
    val seList = SEList[CommandEvent]()
    val it     = seList.iterator
    events.foreach(e => seList.add(e))
    seList.add(CommandEvent.sessionClosed)
    new Controller(it, renderChanges, modelComponents, Map(initialModel.asInstanceOf[Model[Any]] -> initialValue), Nil)

  test("will throw an exception if there is a duplicate key"):
    an[IllegalArgumentException] should be thrownBy
      newController(Model[Int], 0, Seq(buttonClick), Seq(button, button)).render().handledEventsIterator

  test("onEvent is called"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(buttonClick), Seq(button))
      .onEvent:
        case ControllerClickEvent[Int @unchecked](_, handled, model) =>
          if model > 1 then handled.terminate else handled.withModel(model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(inputChange), Seq(input))
      .onEvent:
        case ControllerChangeEvent[Int @unchecked](_, handled, newValue, model) =>
          if model > 1 then handled.terminate else handled.withModel(model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matched for change"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(inputChange), Seq(input))
      .onEvent:
        case event: ControllerClickEvent[Int @unchecked] =>
          import event.*
          handled.withModel(5)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  test("onEvent is called for change/boolean"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(checkBoxChange), Seq(checkbox))
      .onEvent:
        case event: ControllerChangeBooleanEvent[Int @unchecked] =>
          import event.*
          if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matches for change/boolean"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(checkBoxChange), Seq(checkbox))
      .onEvent:
        case event: ControllerClickEvent[Int @unchecked] =>
          import event.*
          handled.withModel(5)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  case class TestClientEvent(i: Int) extends ClientEvent

  test("onEvent is called for ClientEvent"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClientEvent[Int @unchecked](handled, event: TestClientEvent, _) =>
          handled.withModel(event.i).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 5))

  test("onEvent when no partial function matches ClientEvent"):
    given Model[Int] = intModel
    newController(intModel, 0, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClickEvent[Int @unchecked](`checkbox`, handled, _) =>
          handled.withModel(5).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  test("onClick is called"):
    given Model[Int] = intModel
    newController(
      intModel,
      0,
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
    given Model[Int] = intModel
    newController(
      intModel,
      0,
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
    given Model[Int] = intModel
    newController(
      intModel,
      0,
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
    given Model[Int] = intModel
    newController(intModel, 0, Seq(buttonClick, buttonClick, buttonClick), Seq(button))
      .onEvent:
        case event: ControllerEvent[Int @unchecked] =>
          if event.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    given Model[Int]                      = intModel
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    val but                               = button.onModelChange: (b, m) =>
      b.withText(s"changed $m")

    val handled = newController(intModel, 0, Seq(buttonClick), Seq(but), renderer)
      .onEvent:
        case event: ControllerEvent[Int @unchecked] =>
          event.handled.withModel(event.model + 1).terminate
      .render()
      .handledEventsIterator
      .toList

    val expected = Seq(but.withText("changed 1"))
    rendered should be(expected)
    handled.map(_.renderedChanges)(1) should be(expected)

  test("rendered are cleared"):
    given Model[Int] = intModel
    val but          = button.onModelChange: (b, m) =>
      if m == 1 then b.withText(s"changed $m") else b

    val handled = newController(intModel, 0, Seq(buttonClick, checkBoxChange), Seq(but, checkbox))
      .onEvent:
        case event: ControllerEvent[Int @unchecked] =>
          val h = event.handled.withModel(event.model + 1)
          if h.model > 1 then h.terminate else h
      .render()
      .handledEventsIterator
      .toList

    val rendered = handled.map(_.renderedChanges)
    rendered.head should be(Nil)
    rendered(1) should be(Seq(but.withText("changed 1")))
    rendered(2) should be(Nil)

  test("components handle events"):
    given Model[Int]  = intModel
    val table         = QuickTable().withRows(
      Seq(
        Seq(
          button.onClick: event =>
            import event.*
            handled.withModel(model + 1).terminate
        )
      )
    )
    val handledEvents = newController(intModel, 0, Seq(buttonClick), Seq(table))
      .render()
      .handledEventsIterator
      .toList

    handledEvents.map(_.model) should be(List(0, 1))

  test("components receive onModelChange"):
    given Model[Int] = intModel
    val called       = new AtomicBoolean(false)
    val table        = QuickTable()
      .withRows(
        Seq(
          Seq(
            button.onClick: event =>
              import event.*
              handled.withModel(model + 1).terminate
          )
        )
      )
      .onModelChange: (table, _) =>
        called.set(true)
        table
    newController(intModel, 0, Seq(buttonClick), Seq(table))
      .render()
      .handledEventsIterator
      .lastOption

    called.get() should be(true)

  test("applies initial model before rendering"):
    given Model[Int] = intModel

    val b = button.onModelChange: (b, m) =>
      b.withText(s"model $m")

    val connectedSession = mock[ConnectedSession]
    newController(intModel, 5, Nil, Seq(b))
      .render()(using connectedSession)

    verify(connectedSession).render(Seq(b.withText("model 5")))

  test("RenderChangesEvent renders changes"):
    given Model[Int] = intModel

    val handledEvents = newController(intModel, 5, Seq(RenderChangesEvent(Seq(button.withText("changed")))), Seq(button))
      .render()
      .handledEventsIterator
      .toList

    handledEvents(1).renderedChanges should be(Seq(button.withText("changed")))

  test("ModelChangeEvent"):

    val handledEvents = newController(stringModel, "v", Seq(ModelChangeEvent(intModel, 6)), Nil).render().handledEventsIterator.toList
    handledEvents(1).modelOf(intModel) should be(6)

  test("onModelChange for different model"):
    val b1 = button.onModelChange(using intModel): (b, m) =>
      b.withText(s"changed $m")

    val handledEvents = newController(stringModel, "v", Seq(ModelChangeEvent(intModel, 6)), Seq(b1)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(b1.withText("changed 6")))
