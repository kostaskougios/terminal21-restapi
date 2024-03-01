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

  object Givens:
    given intModel: Model[Int]       = Model[Int]("int-model")
    given stringModel: Model[String] = Model[String]("string-model")

  def newController[M](
      initialModel: Model[M],
      initialValue: M,
      events: => Seq[CommandEvent],
      modelComponents: Seq[UiElement],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller =
    val seList = SEList[CommandEvent]()
    val it     = seList.iterator
    events.foreach(e => seList.add(e))
    seList.add(CommandEvent.sessionClosed)
    new Controller(it, renderChanges, modelComponents, Map.empty, Nil).model(using initialModel)(initialValue)

  test("will throw an exception if there is a duplicate key"):
    an[IllegalArgumentException] should be thrownBy
      newController(Model[Int], 0, Seq(buttonClick), Seq(button, button)).render().handledEventsIterator

  test("onEvent is called"):
    import Givens.intModel
    newController(intModel, 0, Seq(buttonClick), Seq(button))
      .onEvent:
        case ControllerClickEvent[Int @unchecked](_, handled) =>
          if handled.model > 1 then handled.terminate else handled.withModel(handled.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    import Givens.intModel
    newController(intModel, 0, Seq(inputChange), Seq(input))
      .onEvent:
        case ControllerChangeEvent[Int @unchecked](_, handled, newValue) =>
          if handled.model > 1 then handled.terminate else handled.withModel(handled.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1))

  test("onEvent not matched for change"):
    import Givens.intModel
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
    import Givens.intModel
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
    import Givens.intModel
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
    import Givens.intModel
    newController(intModel, 0, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClientEvent[Int @unchecked](handled, event: TestClientEvent) =>
          handled.withModel(event.i).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 5))

  test("onEvent when no partial function matches ClientEvent"):
    import Givens.intModel
    newController(intModel, 0, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClickEvent[Int @unchecked](`checkbox`, handled) =>
          handled.withModel(5).terminate
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 0))

  test("onClick is called"):
    import Givens.intModel
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
    import Givens.intModel
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
    import Givens.intModel
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
    import Givens.intModel
    newController(intModel, 0, Seq(buttonClick, buttonClick, buttonClick), Seq(button))
      .onEvent:
        case event: ControllerEvent[Int @unchecked] =>
          if event.handled.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.handled.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model)
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    import Givens.intModel
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    val but                               = button.onModelChangeRender: (b, m) =>
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
    import Givens.intModel
    val but = button.onModelChangeRender: (b, m) =>
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
    import Givens.intModel
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
    import Givens.intModel
    val called = new AtomicBoolean(false)
    val table  = QuickTable()
      .withRows(
        Seq(
          Seq(
            button.onClick: event =>
              import event.*
              handled.withModel(model + 1).terminate
          )
        )
      )
      .onModelChangeRender: (table, _) =>
        called.set(true)
        table
    newController(intModel, 0, Seq(buttonClick), Seq(table))
      .render()
      .handledEventsIterator
      .lastOption

    called.get() should be(true)

  test("applies initial model before rendering"):
    import Givens.intModel

    val b = button.onModelChangeRender: (b, m) =>
      b.withText(s"model $m")

    val connectedSession = mock[ConnectedSession]
    newController(intModel, 5, Nil, Seq(b))
      .render()(using connectedSession)

    verify(connectedSession).render(Seq(b.withText("model 5")))

  test("RenderChangesEvent renders changes"):
    import Givens.intModel

    val handledEvents = newController(intModel, 5, Seq(RenderChangesEvent(Seq(button.withText("changed")))), Seq(button))
      .render()
      .handledEventsIterator
      .toList

    handledEvents(1).renderedChanges should be(Seq(button.withText("changed")))

  test("ModelChangeEvent"):
    import Givens.given
    val handledEvents = newController(stringModel, "v", Seq(ModelChangeEvent(intModel, 6)), Nil).model(using intModel)(5).render().handledEventsIterator.toList
    handledEvents(1).modelOf(intModel) should be(6)

  test("onModelChange for different model"):
    import Givens.given
    val b1 = button.onModelChangeRender(using intModel): (b, m) =>
      b.withText(s"changed $m")

    val handledEvents = newController(stringModel, "v", Seq(ModelChangeEvent(intModel, 6)), Seq(b1)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(b1.withText("changed 6")))

  test("onModelChange when model change triggered by event"):
    import Givens.given
    val b1            = Button().onModelChangeRender(using intModel): (b, m) =>
      b.withText(s"changed $m")
    val b2            = button.onClick(using intModel): event =>
      event.handled.mapModel(_ + 1)
    val handledEvents = newController(intModel, 5, Seq(buttonClick), Seq(b1, b2)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(b1.withText("changed 6")))
