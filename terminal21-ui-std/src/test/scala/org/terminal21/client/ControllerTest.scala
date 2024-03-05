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
