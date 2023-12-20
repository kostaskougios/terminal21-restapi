package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.greenProps

object Forms:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val email = Input(`type` = "email", value = "Hello world!")

    val checkbox2 = Checkbox(text = "Check 2", defaultChecked = true)

    val checkbox1 = Checkbox(text = "Check 1")
    checkbox1.onChange: newValue =>
      println(s"checkbox1 = $newValue")
      checkbox2.isDisabled = newValue
      session.render()

    val radioGroup = RadioGroup(defaultValue = "2").withChildren(
      HStack().withChildren(
        Radio(value = "1", text = "first"),
        Radio(value = "2", text = "second"),
        Radio(value = "3", text = "third")
      )
    )

    radioGroup.onChange: newValue =>
      println(s"radioGroup value=$newValue , radioGroup.value=${radioGroup.value}")

    Seq(
      Box(text = "Forms", props = greenProps),
      FormControl().withChildren(
        FormLabel(text = "Email address"),
        email,
        FormHelperText(text = "We'll never share your email.")
      ),
      HStack().withChildren(
        checkbox1,
        checkbox2
      ),
      ButtonGroup(variant = Some("outline"), spacing = Some("24")).withChildren(
        Button(text = "Save", colorScheme = Some("red")),
        Button(text = "Cancel")
      ),
      radioGroup
    )
