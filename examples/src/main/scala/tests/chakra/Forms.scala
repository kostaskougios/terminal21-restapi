package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.greenProps

object Forms:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val status = Box(text = "This will reflect any changes in the form.")
    val email  = Input(`type` = "email", value = "Hello world!")
    email.onChange: newValue =>
      status.text = s"email input new value = $newValue, verify email.value = ${email.value}"
      session.render()

    val checkbox2 = Checkbox(text = "Check 2", defaultChecked = true)
    checkbox2.onChange: newValue =>
      status.text = s"checkbox2 checked is $newValue , verify checkbox2.checked = ${checkbox2.checked}"
      session.render()

    val checkbox1 = Checkbox(text = "Check 1")
    checkbox1.onChange: newValue =>
      checkbox2.isDisabled = newValue
      status.text = s"checkbox1 checked is $newValue , verify checkbox1.checked = ${checkbox1.checked}"
      session.render()

    val radioGroup = RadioGroup(defaultValue = "2").withChildren(
      HStack().withChildren(
        Radio(value = "1", text = "first"),
        Radio(value = "2", text = "second"),
        Radio(value = "3", text = "third")
      )
    )

    radioGroup.onChange: newValue =>
      status.text = s"radioGroup newValue=$newValue , verify radioGroup.value=${radioGroup.value}"
      session.render()

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
        Button(text = "Save", colorScheme = Some("red"))
          .onClick: () =>
            status.text = "Saved clicked"
            session.render()
        ,
        Button(text = "Cancel")
          .onClick: () =>
            status.text = "Cancel clicked"
            session.render()
      ),
      radioGroup,
      status
    )
