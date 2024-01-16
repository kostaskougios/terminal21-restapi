#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.mathjax.*

Sessions.withNewSession("mathjax", "MathJax Example", MathJaxLib /* note we need to register the MathJaxLib in order to use it */ ): session =>
  given ConnectedSession = session
  Seq(
    MathJax(
      expression = """When \(a \ne 0\), there are two solutions to \(ax^2 + bx + c = 0\) and they are $$x = {-b \pm \sqrt{b^2-4ac} \over 2a}.$$"""
    ),
    MathJax(
      expression = """
          |when \(a \ne 0\), there are two solutions to \(x = {-b \pm \sqrt{b^2-4ac} \over 2a}.\)
          |Aenean vel velit a lacus lacinia pulvinar. Morbi eget ex et tellus aliquam molestie sit amet eu diam.
          |Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas tellus enim, tempor non efficitur et, rutrum efficitur metus.
          |Nulla scelerisque, mauris sit amet accumsan iaculis, elit ipsum suscipit lorem, sed fermentum nunc purus non tellus.
          |Aenean congue accumsan tempor. \(x = {-b \pm \sqrt{b^2-4ac} \over 2a}.\) maecenas vitae commodo tortor. Aliquam erat volutpat. Etiam laoreet malesuada elit sed vestibulum.
          |Etiam consequat congue fermentum. Vivamus dapibus scelerisque ipsum eu tempus. Integer non pulvinar nisi.
          |Morbi ultrices sem quis nisl convallis, ac cursus nunc condimentum. Orci varius natoque penatibus et magnis dis parturient montes,
          |nascetur ridiculus mus.
          |""".stripMargin
    )
  ).render()
  session.leaveSessionOpenAfterExiting()
