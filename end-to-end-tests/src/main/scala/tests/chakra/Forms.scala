package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Forms:
  def components(m: ChakraModel, events: Events): Seq[UiElement] =
    val okIcon    = CheckCircleIcon(color = Some("green"))
    val notOkIcon = WarningTwoIcon(color = Some("red"))

    val email       = Input(key = "email", `type` = "email", defaultValue = m.email)
    val description = Textarea(key = "textarea", placeholder = "Please enter a few things about you", defaultValue = "desc")
    val select1     = Select(key = "male/female", placeholder = "Please choose")
      .withChildren(
        Option_(text = "Male", value = "male"),
        Option_(text = "Female", value = "female")
      )
    val select2     =
      Select(key = "select-first-second", defaultValue = "1", bg = Some("tomato"), color = Some("black"), borderColor = Some("yellow")).withChildren(
        Option_(text = "First", value = "1"),
        Option_(text = "Second", value = "2")
      )
    val password    = Input(key = "password", `type` = "password", defaultValue = "mysecret")
    val dob         = Input(key = "dob", `type` = "datetime-local")
    val color       = Input(key = "color", `type` = "color")
    val checkbox2   = Checkbox(key = "cb2", text = "Check 2", defaultChecked = true)
    val checkbox1   = Checkbox(key = "cb1", text = "Check 1")

    val newM         = m.copy(
      email = events.changedValue(email).getOrElse(m.email)
    )
    val switch1      = Switch(key = "sw1", text = "Switch 1")
    val switch2      = Switch(key = "sw2", text = "Switch 2", defaultChecked = true)
    val radioGroup   = RadioGroup(key = "radio", defaultValue = "2")
      .withChildren(
        HStack().withChildren(
          Radio(value = "1", text = "first"),
          Radio(value = "2", text = "second"),
          Radio(value = "3", text = "third")
        )
      )
    val saveButton   = Button(key = "save-button", text = "Save", colorScheme = Some("red"))
    val cancelButton = Button(key = "cancel-button", text = "Cancel")
    val formStatus   =
      events
        .changedValue(email)
        .map(v => s"email input new value = $v")
        .toSeq ++ events
        .changedValue(description)
        .map(v => s"description input new value = $v") ++ events
        .changedValue(select1)
        .map(v => s"select1 input new value = $v") ++ events
        .changedValue(dob)
        .map(v => s"dob = $v") ++ events
        .changedValue(color)
        .map(v => s"color = $v") ++ events
        .changedValue(checkbox2)
        .map(v => s"checkbox2 checked is $v") ++ events
        .changedValue(checkbox1)
        .map(v => s"checkbox1 checked is $v") ++ events
        .changedValue(switch1)
        .map(v => s"switch1 checked is $v") ++ events
        .changedValue(radioGroup)
        .map(v => s"radioGroup newValue is $v") ++ events
        .ifClicked(saveButton, "Saved clicked") ++ events
        .ifClicked(cancelButton, "Cancel clicked")
        .headOption
        .getOrElse("This will reflect any changes in the form.")

    val status = Box(text = formStatus)

    val emailRightAddOn = InputRightAddon()
      .withChildren(if newM.email.contains("@") then okIcon else notOkIcon)

    Seq(
      commonBox(text = "Forms"),
      FormControl().withChildren(
        FormLabel(text = "Test-Email-Address"),
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
        saveButton,
        cancelButton
      ),
      radioGroup,
      status
    )
