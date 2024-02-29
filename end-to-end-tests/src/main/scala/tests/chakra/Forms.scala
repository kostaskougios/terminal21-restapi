package tests.chakra

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.chakra.*
import tests.chakra.Common.*

object Forms:
  def components(m: ChakraModel)(using Model[ChakraModel]): Seq[UiElement] =
    val status    = Box().onModelChange: (b, m) =>
      b.withText(m.formStatus)
    val okIcon    = CheckCircleIcon(color = Some("green"))
    val notOkIcon = WarningTwoIcon(color = Some("red"))

    val emailRightAddOn = InputRightAddon().onModelChange: (i, m) =>
      i.withChildren(if m.email.contains("@") then okIcon else notOkIcon)

    val email = Input(key = "email", `type` = "email", defaultValue = m.email)
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(email = newValue, formStatus = s"email input new value = $newValue"))

    val description = Textarea(key = "textarea", placeholder = "Please enter a few things about you", defaultValue = "desc")
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"description input new value = $newValue"))

    val select1 = Select(key = "male/female", placeholder = "Please choose")
      .withChildren(
        Option_(text = "Male", value = "male"),
        Option_(text = "Female", value = "female")
      )
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"select1 input new value = $newValue"))

    val select2 =
      Select(key = "select-first-second", defaultValue = "1", bg = Some("tomato"), color = Some("black"), borderColor = Some("yellow")).withChildren(
        Option_(text = "First", value = "1"),
        Option_(text = "Second", value = "2")
      )

    val password = Input(key = "password", `type` = "password", defaultValue = "mysecret")
    val dob      = Input(key = "dob", `type` = "datetime-local")
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"dob = $newValue"))

    val color = Input(key = "color", `type` = "color")
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"color = $newValue"))

    val checkbox2 = Checkbox(key = "cb2", text = "Check 2", defaultChecked = true)
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"checkbox2 checked is $newValue"))

    val checkbox1 = Checkbox(key = "cb1", text = "Check 1")
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"checkbox1 checked is $newValue"))

    val switch1 = Switch(key = "sw1", text = "Switch 1")
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"switch1 checked is $newValue"))
    val switch2 = Switch(key = "sw2", text = "Switch 2", defaultChecked = true)

    val radioGroup = RadioGroup(key = "radio", defaultValue = "2")
      .withChildren(
        HStack().withChildren(
          Radio(value = "1", text = "first"),
          Radio(value = "2", text = "second"),
          Radio(value = "3", text = "third")
        )
      )
      .onChange: event =>
        import event.*
        handled.withModel(_.copy(formStatus = s"radioGroup newValue=$newValue"))

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
        Button(key = "save-button", text = "Save", colorScheme = Some("red"))
          .onClick: event =>
            import event.*
            handled.withModel(_.copy(formStatus = s"Saved clicked"))
        ,
        Button(key = "cancel-button", text = "Cancel")
          .onClick: event =>
            import event.*
            handled.withModel(_.copy(formStatus = s"Cancel clicked"))
      ),
      radioGroup,
      status
    )
