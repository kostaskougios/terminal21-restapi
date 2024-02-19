#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.components.chakra.*

Sessions
  .withNewSession("on-change-example", "On Change event handler")
  .connect: session =>
    given ConnectedSession = session

    val output = Paragraph(text = "Please modify the email.")
    val email = Input(`type` = "email", defaultValue = "my@email.com").onChange: v =>
      output.withText(s"Email value : $v").renderChanges()

    Seq(
      FormControl().withChildren(
        FormLabel(text = "Email address"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(EmailIcon()),
          email
        ),
        FormHelperText(text = "We'll never share your email.")
      ),
      output
    ).render()

    session.waitTillUserClosesSession()
