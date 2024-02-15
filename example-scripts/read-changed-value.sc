#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.components.chakra.*

Sessions
  .withNewSession("read-changed-value-example", "Read Changed Value")
  .connect: session =>
    given ConnectedSession = session

    val email = Input(`type` = "email", value = "my@email.com")
    val output = Box()

    Seq(
      FormControl().withChildren(
        FormLabel(text = "Email address"),
        InputGroup().withChildren(
          InputLeftAddon().withChildren(EmailIcon()),
          email
        ),
        FormHelperText(text = "We'll never share your email.")
      ),
      Button(text = "Read Value").onClick: () =>
        val value = email.current.value
        output.current.addChildren(Paragraph(text = s"The value now is $value")).renderChanges()
      ,
      output
    ).render()

    session.waitTillUserClosesSession()
