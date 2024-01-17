package tests.chakra

import org.terminal21.client.ConnectedSession
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Forms:
  def components(using session: ConnectedSession): Seq[UiElement] =
    val status    = Box(text = "This will reflect any changes in the form.")
    val okIcon    = CheckCircleIcon(color = Some("green"))
    val notOkIcon = WarningTwoIcon(color = Some("red"))

    val emailRightAddOn = InputRightAddon().withChildren(okIcon)

    val email = Input(`type` = "email", value = "my@email.com")
    email.onChange: newValue =>
      Seq(
        status.withText(s"email input new value = $newValue, verify email.value = ${email.current.value}"),
        if newValue.contains("@") then emailRightAddOn.withChildren(okIcon) else emailRightAddOn.withChildren(notOkIcon)
      ).renderChanges()

    val description = Textarea(placeholder = "Please enter a few things about you")
    description.onChange: newValue =>
      status.withText(s"description input new value = $newValue, verify description.value = ${description.current.value}").renderChanges()

    val select1 = Select(placeholder = "Please choose").withChildren(
      Option_(text = "Male", value = "male"),
      Option_(text = "Female", value = "female")
    )

    select1.onChange: newValue =>
      status.withText(s"select1 input new value = $newValue, verify select1.value = ${select1.current.value}").renderChanges()

    val select2 = Select(value = "1", bg = Some("tomato"), color = Some("black"), borderColor = Some("yellow")).withChildren(
      Option_(text = "First", value = "1"),
      Option_(text = "Second", value = "2")
    )

    val password = Input(`type` = "password", value = "mysecret")
    val dob      = Input(`type` = "datetime-local")
    dob.onChange: newValue =>
      status.withText(s"dob = $newValue , verify dob.value = ${dob.current.value}").renderChanges()

    val color = Input(`type` = "color")

    color.onChange: newValue =>
      status.withText(s"color = $newValue , verify color.value = ${color.current.value}").renderChanges()

    val checkbox2 = Checkbox(text = "Check 2", defaultChecked = true)
    checkbox2.onChange: newValue =>
      status.withText(s"checkbox2 checked is $newValue , verify checkbox2.checked = ${checkbox2.current.checked}").renderChanges()

    val checkbox1 = Checkbox(text = "Check 1")
    checkbox1.onChange: newValue =>
      Seq(
        status.withText(s"checkbox1 checked is $newValue , verify checkbox1.checked = ${checkbox1.current.checked}"),
        checkbox2.withIsDisabled(newValue)
      ).renderChanges()

    val switch1 = Switch(text = "Switch 1")
    val switch2 = Switch(text = "Switch 2", defaultChecked = true)

    switch1.onChange: newValue =>
      Seq(
        status.withText(s"switch1 checked is $newValue , verify switch1.checked = ${switch1.current.checked}"),
        switch2.withIsDisabled(newValue)
      ).renderChanges()

    val radioGroup = RadioGroup(defaultValue = "2").withChildren(
      HStack().withChildren(
        Radio(value = "1", text = "first"),
        Radio(value = "2", text = "second"),
        Radio(value = "3", text = "third")
      )
    )

    radioGroup.onChange: newValue =>
      status.withText(s"radioGroup newValue=$newValue , verify radioGroup.value=${radioGroup.current.value}").renderChanges()

    Seq(
      commonBox(text = "Forms"),
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
        select1,
        select2
      ),
      HStack().withChildren(
        checkbox1,
        checkbox2
      ),
      HStack().withChildren(
        switch1,
        switch2
      ),
      ButtonGroup(variant = Some("outline"), spacing = Some("24")).withChildren(
        Button(text = "Save", colorScheme = Some("red"))
          .onClick: () =>
            status
              .withText(
                s"Saved clicked. Email = ${email.current.value}, password = ${password.current.value}, dob = ${dob.current.value}, check1 = ${checkbox1.current.checked}, check2 = ${checkbox2.current.checked}, radio = ${radioGroup.current.value}"
              )
              .renderChanges(),
        Button(text = "Cancel")
          .onClick: () =>
            status.withText("Cancel clicked").renderChanges()
      ),
      radioGroup,
      status
    )
