package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.UiElement
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.greenProps

object Forms:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val status    = Box(text = "This will reflect any changes in the form.")
    val okIcon    = CheckCircleIcon(color = Some("green"))
    val notOkIcon = WarningTwoIcon(color = Some("red"))

    val emailRightAddOn = InputRightAddon().withChildren(okIcon)

    val email = Input(`type` = "email", value = "my@email.com")
    email.onChange: newValue =>
      status.text = s"email input new value = $newValue, verify email.value = ${email.value}"
      if newValue.contains("@") then emailRightAddOn.children = Seq(okIcon) else emailRightAddOn.children = Seq(notOkIcon)
      session.render()

    val description = Textarea(placeholder = "Please enter a few things about you")
    description.onChange: newValue =>
      status.text = s"description input new value = $newValue, verify description.value = ${description.value}"
      session.render()

    val password = Input(`type` = "password", value = "mysecret")
    val dob      = Input(`type` = "datetime-local")
    dob.onChange: newValue =>
      status.text = s"dob = $newValue , verify dob.value = ${dob.value}"
      session.render()

    val color = Input(`type` = "color")

    color.onChange: newValue =>
      status.text = s"color = $newValue , verify color.value = ${color.value}"
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
        InputGroup().withChildren(
          InputLeftAddon().withChildren(EmailIcon()),
          email,
          emailRightAddOn
        ),
        FormHelperText(text = "We'll never share your email.")
      ),
      FormControl().withChildren(
        FormLabel(text = "Description"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(EditIcon()),
          description
        ),
        FormHelperText(text = "We'll never share your email.")
      ),
      FormControl().withChildren(
        FormLabel(text = "Password"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(ViewOffIcon()),
          password
        ),
        FormHelperText(text = "Don't share with anyone")
      ),
      FormControl().withChildren(
        FormLabel(text = "Date of birth"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(CalendarIcon()),
          dob
        )
      ),
      FormControl().withChildren(
        FormLabel(text = "Color"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(DragHandleIcon()),
          color
        )
      ),
      HStack().withChildren(
        checkbox1,
        checkbox2
      ),
      ButtonGroup(variant = Some("outline"), spacing = Some("24")).withChildren(
        Button(text = "Save", colorScheme = Some("red"))
          .onClick: () =>
            status.text =
              s"Saved clicked. Email = ${email.value}, password = ${password.value}, dob = ${dob.value}, check1 = ${checkbox1.checked}, check2 = ${checkbox2.checked}, radio = ${radioGroup.value}"
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
