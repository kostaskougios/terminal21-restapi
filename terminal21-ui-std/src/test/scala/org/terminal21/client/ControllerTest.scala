package org.terminal21.client

import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.mockito.MockitoSugar.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.{Box, Button, Checkbox, QuickTable}
import org.terminal21.client.components.std.{Input, Paragraph}
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

  val intModel: Model[Int]       = Model[Int]("int-model")
  val stringModel: Model[String] = Model[String]("string-model")

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
    new Controller(it, renderChanges, modelComponents, Map.empty, Nil).withModel(initialModel, initialValue)

  test("will throw an exception if there is a duplicate key"):
    an[IllegalArgumentException] should be thrownBy
      newController(Model[Int], 0, Seq(buttonClick), Seq(button, button)).render().handledEventsIterator

  test("onEvent is called"):
    newController(intModel, 0, Seq(buttonClick), Seq(button))
      .onEvent:
        case ControllerClickEvent[Int @unchecked](_, handled) =>
          if handled.model > 1 then handled.terminate else handled.withModel(handled.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 1))

  test("onEvent is called for change"):
    newController(intModel, 0, Seq(inputChange), Seq(input))
      .onEvent:
        case ControllerChangeEvent[Int @unchecked](_, handled, newValue) =>
          if handled.model > 1 then handled.terminate else handled.withModel(handled.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 1))

  test("onEvent not matched for change"):
    newController(intModel, 0, Seq(inputChange), Seq(input))
      .onEvent:
        case event: ControllerClickEvent[Int @unchecked] =>
          import event.*
          handled.withModel(5)
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 0))

  test("onEvent is called for change/boolean"):
    newController(intModel, 0, Seq(checkBoxChange), Seq(checkbox))
      .onEvent:
        case event: ControllerChangeBooleanEvent[Int @unchecked] =>
          import event.*
          if event.model > 1 then handled.terminate else handled.withModel(event.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 1))

  test("onEvent not matches for change/boolean"):
    newController(intModel, 0, Seq(checkBoxChange), Seq(checkbox))
      .onEvent:
        case event: ControllerClickEvent[Int @unchecked] =>
          import event.*
          handled.withModel(5)
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 0))

  case class TestClientEvent(i: Int) extends ClientEvent

  test("onEvent is called for ClientEvent"):
    newController(intModel, 0, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClientEvent[Int @unchecked](handled, event: TestClientEvent) =>
          handled.withModel(event.i).terminate
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 5))

  test("onEvent when no partial function matches ClientEvent"):
    newController(intModel, 0, Seq(TestClientEvent(5)), Seq(button))
      .onEvent:
        case ControllerClickEvent[Int @unchecked](`checkbox`, handled) =>
          handled.withModel(5).terminate
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 0))

  test("onClick is called"):
    newController(
      intModel,
      0,
      Seq(buttonClick),
      Seq(
        button.onClick(intModel): event =>
          event.handled.withModel(100).terminate
      )
    ).render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 100))

  test("onClick is called for multiple models"):
    newController(
      intModel,
      0,
      Seq(buttonClick),
      Seq(
        button
          .onClick(intModel): event =>
            event.handled.withModel(100).terminate
          .onClick(stringModel): event =>
            event.handled.withModel("new").terminate
      )
    ).withModel(stringModel, "old")
      .render()
      .handledEventsIterator
      .map(h => (h.model(intModel), h.model(stringModel)))
      .toList should be(List((0, "old"), (100, "new")))

  test("onChange is called"):
    newController(
      intModel,
      0,
      Seq(inputChange),
      Seq(
        input.onChange(intModel): event =>
          event.handled.withModel(100).terminate
      )
    ).render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 100))

  test("onChange/boolean is called"):
    newController(
      intModel,
      0,
      Seq(checkBoxChange),
      Seq(
        checkbox.onChange(intModel): event =>
          event.handled.withModel(100).terminate
      )
    ).render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 100))

  test("terminate is obeyed and latest model state is iterated"):
    newController(intModel, 0, Seq(buttonClick, buttonClick, buttonClick), Seq(button))
      .onEvent:
        case event: ControllerEvent[Int @unchecked] =>
          if event.handled.model > 1 then event.handled.terminate.withModel(100) else event.handled.withModel(event.handled.model + 1)
      .render()
      .handledEventsIterator
      .map(_.model(intModel))
      .toList should be(List(0, 1, 2, 100))

  test("changes are rendered"):
    var rendered                          = Seq.empty[UiElement]
    def renderer(s: Seq[UiElement]): Unit = rendered = s
    val but                               = button.onModelChangeRender(intModel): (b, m) =>
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
    val but = button.onModelChangeRender(intModel): (b, m) =>
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
    val table         = QuickTable().withRows(
      Seq(
        Seq(
          button.onClick(intModel): event =>
            import event.*
            handled.withModel(model + 1).terminate
        )
      )
    )
    val handledEvents = newController(intModel, 0, Seq(buttonClick), Seq(table))
      .render()
      .handledEventsIterator
      .toList

    handledEvents.map(_.model(intModel)) should be(List(0, 1))

  test("components receive onModelChange"):
    val called = new AtomicBoolean(false)
    val table  = QuickTable()
      .withRows(
        Seq(
          Seq(
            button.onClick(intModel): event =>
              import event.*
              handled.withModel(model + 1).terminate
          )
        )
      )
      .onModelChangeRender(intModel): (table, _) =>
        called.set(true)
        table
    newController(intModel, 0, Seq(buttonClick), Seq(table))
      .render()
      .handledEventsIterator
      .lastOption

    called.get() should be(true)

  test("applies initial model before rendering"):
    val b = button.onModelChangeRender(intModel): (b, m) =>
      b.withText(s"model $m")

    val connectedSession = mock[ConnectedSession]
    newController(intModel, 5, Nil, Seq(b))
      .render()(using connectedSession)

    verify(connectedSession).render(Seq(b.withText("model 5")))

  test("applies multiple initial model before rendering"):
    val b = button
      .onModelChangeRender(intModel): (b, m) =>
        b.withText(s"model $m")
      .onModelChangeRender(stringModel): (b, m) =>
        b.withText(b.text + s" model $m")

    val connectedSession = mock[ConnectedSession]
    newController(intModel, 5, Nil, Seq(b))
      .withModel(stringModel, "x")
      .render()(using connectedSession)

    verify(connectedSession).render(Seq(b.withText("model 5 model x")))

  test("RenderChangesEvent renders changes"):
    val handledEvents = newController(intModel, 5, Seq(RenderChangesEvent(Seq(button.withText("changed")))), Seq(button))
      .render()
      .handledEventsIterator
      .toList

    handledEvents(1).renderedChanges should be(Seq(button.withText("changed")))

  test("ModelChangeEvent"):
    val handledEvents = newController(stringModel, "v", Seq(ModelChangeEvent(intModel, 6)), Nil).withModel(intModel, 5).render().handledEventsIterator.toList
    handledEvents(1).modelOf(intModel) should be(6)

  test("onModelChange for different model"):
    val b1 = button.onModelChangeRender(intModel): (b, m) =>
      b.withText(s"changed $m")

    val handledEvents = newController(stringModel, "v", Seq(ModelChangeEvent(intModel, 6)), Seq(b1)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(b1.withText("changed 6")))

  test("onModelChange when model change triggered by event"):
    val b1            = Button().onModelChangeRender(intModel): (b, m) =>
      b.withText(s"changed $m")
    val b2            = button.onClick(intModel): event =>
      event.handled.mapModel(_ + 1)
    val handledEvents = newController(intModel, 5, Seq(buttonClick), Seq(b1, b2)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(b1.withText("changed 6")))

  test("onModelChange hierarchy"):
    val b1  = Button()
      .onModelChangeRender(intModel): (b, m) =>
        b.withText(s"changed $m")
      .onClick(stringModel): event => // does nothing, just simulates that this button is actually for a different model
        event.handled
    val b2  = button.onClick(intModel): event =>
      event.handled.mapModel(_ + 1)
    val box = Box().withChildren(b1, Paragraph().withChildren(b2))

    val handledEvents = newController(intModel, 5, Seq(buttonClick), Seq(box)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(b1.withText("changed 6")))

  test("onModelChange hierarchy with component"):
    val t1  = QuickTable()
      .onModelChangeRender(intModel): (table, m) =>
        table.withRows(Seq(Seq(s"changed $m")))
    val b2  = button.onClick(intModel): event =>
      event.handled.mapModel(_ + 1)
    val box = Box().withChildren(t1, Paragraph().withChildren(b2))

    val handledEvents = newController(intModel, 5, Seq(buttonClick), Seq(box)).render().handledEventsIterator.toList

    handledEvents(1).renderedChanges should be(Seq(t1.withRows(Seq(Seq(s"changed 6")))))
