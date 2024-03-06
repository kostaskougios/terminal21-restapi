package org.terminal21.client

import org.mockito.Mockito
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar.*
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import org.terminal21.client.components.std.Input
import org.terminal21.collections.SEList
import org.terminal21.model.{CommandEvent, OnChange, OnClick}
import org.scalatest.matchers.should.Matchers.*

class ControllerTest extends AnyFunSuiteLike:
  val button             = Button("b1")
  val buttonClick        = OnClick(button.key)
  val input              = Input("i1")
  val inputChange        = OnChange(input.key, "new-value")
  val checkbox           = Checkbox("c1")
  val checkBoxChange     = OnChange(checkbox.key, "true")
  given ConnectedSession = ConnectedSessionMock.newConnectedSessionMock

  def newController[M](
      events: Seq[CommandEvent],
      materializer: ModelViewMaterialized[M],
      renderChanges: Seq[UiElement] => Unit = _ => ()
  ): Controller[M] =
    val seList = SEList[CommandEvent]()
    val it     = seList.iterator
    events.foreach(e => seList.add(e))
    seList.add(CommandEvent.sessionClosed)
    new Controller(it, renderChanges, materializer)

  test("model updated"):
    def components(m: Int, events: Events) = MV(m + 1, Box())
    newController(Seq(buttonClick), components).render(0).iterator.map(_.model).toList should be(List(1, 2))

  test("model when terminated"):
    def components(m: Int, events: Events) =
      MV(100, Seq(Box()), terminate = true)
    newController(Seq(buttonClick), components).render(0).iterator.map(_.model).toList should be(List(100))

  test("view updated"):
    def components(m: Int, events: Events) = MV(m + 1, Box(text = m.toString))
    newController(Seq(buttonClick), components).render(0).iterator.map(_.view).toList should be(Seq(Seq(Box(text = "0")), Seq(Box(text = "1"))))

  test("renderChanges() not invoked if no UI changed"):
    def components(m: Int, events: Events) =
      MV(
        m + 1,
        Box().withChildren(button, input, checkbox)
      )

    var rendered                          = List.empty[Seq[UiElement]]
    def renderChanges(es: Seq[UiElement]) =
      rendered = rendered :+ es

    newController(Seq(buttonClick, checkBoxChange, inputChange), components, renderChanges).render(0).iterator.map(_.model).toList should be(List(1, 2, 3, 4))
    rendered.size should be(1)

  test("renderChanges() invoked if UI changed"):

    def components(m: Int, events: Events) =
      MV(
        m + 1,
        Box(text = s"m=$m").withChildren(button, input, checkbox)
      )

    var rendered = List.empty[Seq[UiElement]]

    def renderChanges(es: Seq[UiElement]) =
      rendered = rendered :+ es

    newController(Seq(buttonClick, checkBoxChange), components, renderChanges).render(0).iterator.map(_.model).toList should be(List(1, 2, 3))
    rendered.size should be(3)

  test("poc"):
    case class Person(id: Int, name: String)
    def personComponent(person: Person, events: Events): MV[Person] =
      val nameInput = Input(s"person-${person.id}", defaultValue = person.name)
      val component = Box()
        .withChildren(
          Text(text = "Name"),
          nameInput
        )
      MV(
        person.copy(
          name = events.changedValue(nameInput, person.name)
        ),
        component
      )

    def peopleComponent(people: Seq[Person], events: Events): MV[Seq[Person]] =
      val peopleComponents = people.map(p => personComponent(p, events))
      val component        = QuickTable("people")
        .withRows(peopleComponents.map(p => Seq(p.view)))
      MV(peopleComponents.map(_.model), component)

    val p1     = Person(10, "person 1")
    val p2     = Person(20, "person 2")
    val people = Seq(p1, p2)
    val mv     = newController(Seq(OnChange("person-10", "changed p10")), peopleComponent)
      .render(people)
      .iterator
      .lastOption
      .get
    mv.model should be(Seq(p1.copy(name = "changed p10"), p2))
