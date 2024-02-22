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

    val emailI = Input(`type` = "email", defaultValue = "my@email.com")
    val email  = emailI.onChange: newValue =>
      Seq(
        status.withText(s"email input new value = $newValue, verify email.value = ${emailI.current.value}"),
        if newValue.contains("@") then emailRightAddOn.withChildren(okIcon) else emailRightAddOn.withChildren(notOkIcon)
      ).renderChanges()

    val descriptionI = Textarea(placeholder = "Please enter a few things about you", defaultValue = "desc")
    val description  = descriptionI.onChange: newValue =>
      status.withText(s"description input new value = $newValue, verify description.value = ${descriptionI.current.value}").renderChanges()

    val select1I = Select(placeholder = "Please choose").withChildren(
      Option_(text = "Male", value = "male"),
      Option_(text = "Female", value = "female")
    )

    val select1 = select1I.onChange: newValue =>
      status.withText(s"select1 input new value = $newValue, verify select1.value = ${select1I.current.value}").renderChanges()

    val select2 = Select(defaultValue = "1", bg = Some("tomato"), color = Some("black"), borderColor = Some("yellow")).withChildren(
      Option_(text = "First", value = "1"),
      Option_(text = "Second", value = "2")
    )

    val password = Input(`type` = "password", defaultValue = "mysecret")
    val dobI     = Input(`type` = "datetime-local")
    val dob      = dobI.onChange: newValue =>
      status.withText(s"dob = $newValue , verify dob.value = ${dobI.current.value}").renderChanges()

    val colorI = Input(`type` = "color")

    val color = colorI.onChange: newValue =>
      status.withText(s"color = $newValue , verify color.value = ${colorI.current.value}").renderChanges()

    val checkbox2I = Checkbox(text = "Check 2", defaultChecked = true)
    val checkbox2  = checkbox2I.onChange: newValue =>
      status.withText(s"checkbox2 checked is $newValue , verify checkbox2.checked = ${checkbox2I.current.checked}").renderChanges()

    val checkbox1I = Checkbox(text = "Check 1")
    val checkbox1  = checkbox1I.onChange: newValue =>
      Seq(
        status.withText(s"checkbox1 checked is $newValue , verify checkbox1.checked = ${checkbox1I.current.checked}"),
        checkbox2.withIsDisabled(newValue)
      ).renderChanges()

    val switch1I = Switch(text = "Switch 1")
    val switch2  = Switch(text = "Switch 2", defaultChecked = true)

    val switch1 = switch1I.onChange: newValue =>
      Seq(
        status.withText(s"switch1 checked is $newValue , verify switch1.checked = ${switch1I.current.checked}"),
        switch2.withIsDisabled(newValue)
      ).renderChanges()

    val radioGroupI = RadioGroup(defaultValue = "2").withChildren(
      HStack().withChildren(
        Radio(value = "1", text = "first"),
        Radio(value = "2", text = "second"),
        Radio(value = "3", text = "third")
      )
    )

    val radioGroup = radioGroupI.onChange: newValue =>
      status.withText(s"radioGroup newValue=$newValue , verify radioGroup.value=${radioGroupI.current.value}").renderChanges()

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
                s"Saved clicked. Email = ${email.current.value}, password = ${password.current.value}, dob = ${dob.current.value}, check1 = ${checkbox1.current.checked}, check2 = ${checkbox2.current.checked}, radio = ${radioGroup.current.value}, switch1 = ${switch1.current.checked}, switch2 = ${switch2.current.checked}"
              )
              .renderChanges(),
        Button(text = "Cancel")
          .onClick: () =>
            status.withText("Cancel clicked").renderChanges()
      ),
      radioGroup,
      status
    )
