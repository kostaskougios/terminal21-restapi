# Terminal21 tutorial

This tutorial assumes you have the terminal 21 server running (as described in the readme file of the project) and you would 
like to learn how to create user interfaces.

This tutorial will use `scala-cli` but the same applies for `sbt` or `mill` projects that use the terminal21 libraries.

All example code is under `example-scripts` of this repo, feel free to check the repo and run them.

## Creating a folder for our scripts

Create a folder and a file `project.scala` into it. This file will help us include the library dependencies and also
scala & jdk version. It should look like this:

```scala
//> using jvm "21"
//> using scala 3

//> using dep io.github.kostaskougios::terminal21-ui-std:_VERSION_
```

Change `_VERSION_` with the terminal 21 latest version.

See [project.scala](../example-scripts/project.scala)

## Creating a hello world app

To do this we can create a [hello-world.sc](../example-scripts/hello-world.sc) in our folder.

```scala
#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*

Sessions.withNewSession("hello-world", "Hello World Example"): session =>
  given ConnectedSession = session

  Paragraph(text = "Hello World!").render()
  session.leaveSessionOpenAfterExiting()
```

The first line, `#!/usr/bin/env -S scala-cli project.scala`, makes our script runnable from the command line.

```shell
chmod +x hello-world.sc

./hello-world.sc
```

I had issues with intellij and this line, so you may want to comment it out while you develop your scripts.

It starts `scala-cli` and also includes `project.scala` so that we get our dependencies, jdk 21 and scala 3 when running our code.


Next it creates a session. Each session has a unique id (globally unique across scripts), in this case `hello-world`. And a session
title, "Hello World Example", that will be displayed on the browser.

```scala
Sessions.withNewSession("hello-world", "Hello World Example"): session =>
  ...
```

![hello-world](images/hello-world.png)

Next is the actual user interface, in this example just a paragraph with a "Hello World!":

```scala
Paragraph(text = "Hello World!").render()
```

The `render()` method sends the UI to the server which in turn sends it to the terminal21 UI so that it is rendered.

Finally because this is just a presentation script (we don't expect any feedback from the user), we can terminate it but
inform terminal21 we want to leave the session open so that the user has a chance to see it.

```scala
session.leaveSessionOpenAfterExiting()
```

When we run our code, it will compile, download dependencies (if needed) and run. It will exit straight away but the UI for our script
will be available in terminal21 UI.

## Updating the UI

Let's create a script that will display a progress bar for some process that will run for some time. The script will update
the progress bar and also give an informative message regarding which stage of the process it is performing.

[progress.sc](../example-scripts/progress.sc)

![progress](images/tutorial/progress.png)

```scala
#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*

Sessions.withNewSession("universe-generation", "Universe Generation Progress"): session =>
  given ConnectedSession = session

  val msg = Paragraph(text = "Generating universe ...")
  val progress = Progress(value = 1)

  Seq(msg, progress).render()

  for i <- 1 to 100 do
    val p = progress.withValue(i)
    val m =
      if i < 10 then msg
      else if i < 30 then msg.withText("Creating atoms")
      else if i < 50 then msg.withText("Big bang!")
      else if i < 80 then msg.withText("Inflating")
      else msg.withText("Life evolution")

    Seq(p, m).renderChanges()
    Thread.sleep(100)

  // clear UI
  session.clear()
  Paragraph(text = "Universe ready!").render()
```

Here we create a paragraph and a progress bar.

```scala
  val msg = Paragraph(text = "Generating universe ...")
  val progress = Progress(value = 1)
```

Then we render them for the first time on screen. When we want to add a new element to the UI, we use the `render()` method. When
we want to update an existing element we use the `renderChanges()` method.

```scala
  Seq(msg, progress).render()
```

Then we have our main loop where the calculations occur. We just use a `Thread.sleep` to simulate that some important task is being calculated. And we
update the progress bar and the message in our paragraph.
```scala
val p = progress.withValue(i)
val m = ... msg.withText("Creating atoms") ...
```

Note the `e.withX()` methods. Those help us change a value on a UI element. We get a copy of the UI element which we can render as an update:

```scala
Seq(p, m).renderChanges()
```

Finally, when the universe is ready, we just clear the UI and render a paragraph before we exit.

```scala
session.clear()
Paragraph(text = "Universe ready!").render()
```
## Handling clicks

Some UI elements allow us to attach an `onClick` handler. When the user clicks the element, our scala code runs.

Let's see for example [on-click.sc](../example-scripts/on-click.sc). We will create a paragraph and a button. When the
user clicks the button, the paragraph text will change and the script will exit.

```scala
#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*

Sessions.withNewSession("on-click-example", "On Click Handler"): session =>
  given ConnectedSession = session

  @volatile var exit = false
  val msg = Paragraph(text = "Waiting for user to click the button")
  val button = Button(text = "Please click me").onClick: () =>
    msg.withText("Button clicked.").renderChanges()
    exit = true

  Seq(msg, button).render()

  session.waitTillUserClosesSessionOr(exit)
```

First we create the paragraph and button. We attach an `onClick` handler on the button:

```scala
  val button = Button(text = "Please click me").onClick: () =>
    msg.withText("Button clicked.").renderChanges()
    exit = true
```
Here we change the paragraph text and also update `exit` to `true`.

Our script waits until var `exit` becomes true and then terminates.

```scala
session.waitTillUserClosesSessionOr(exit)
```

Now if we run it with `./on-click.sc` and click the button, the script will terminate.

