# Terminal21 tutorial

Terminal21 consists of a server and user created scala scripts/apps. The scala scripts contain all the code to create
user interfaces and react to clicks, changed input boxes etc. The terminal21 libraries send the UI's to the server which
in turn renders those as react components (but no js or react coding is required from the user).

For a glimpse on what can be done with terminal21, please have a look at the [terminal21 youtube channel](https://www.youtube.com/@terminal21-gf1oh/videos).

Terminal21 is not meant as a way to create websites. It is rather meant to give UI's to the odd jobs that has to be 
performed by scripts and where it would require a lot of effort to create a dedicated web server with a UI. It is perfect
for scripting for i.e. those internal odd tasks that have to be performed at your workplace or even for things you would
like to do on your box or even maybe to present some code of yours running with a UI rather than a powerpoint
presentation. And you won't have to write a single line of html or javascript.

This tutorial will use `scala-cli` but the same applies for `sbt` or `mill` projects that use the terminal21 libraries. If you
have `scala-cli` installed on your box, you're good to go, there are no other requirements to run terminal21 scripts. Jdk and
dependencies will be downloaded by `scala-cli` for us.

All example code is under `example-scripts` of this repo, feel free to checkout the repo and run them:

```shell
git clone https://github.com/kostaskougios/terminal21-restapi.git
cd terminal21-restapi/example-scripts

# start the server
./server.sc
# ... it will download dependencies & jdk and start the server. Point your browser to http://localhost:8080/ui/

# Open an other terminal window and
./hello-world.sc
# Have a look at your browser now.
```

## Starting the terminal21 server

The easiest way to start the terminal21 server is to have a `scala-cli` script on the box where the server will run:

[server.sc](../example-scripts/server.sc)

```scala
#!/usr/bin/env -S scala-cli

//> using jvm "21"
//> using scala 3
//> using javaOpt -Xmx128m
//> using dep io.github.kostaskougios::terminal21-server:_VERSION_

import org.terminal21.server.Terminal21Server

Terminal21Server.start()
```
Change `_VERSION_` with the terminal 21 latest version: ![artifact](https://img.shields.io/maven-central/v/io.github.kostaskougios/terminal21-server_3)

Now run this with `./server.sc` and the server will start and also print some useful information.

## Creating a folder for our scripts

Create a folder and a file `project.scala` into it. This file will help us include the library dependencies and also
scala & jdk version. It should look like this:

```scala
//> using jvm "21"
//> using scala 3

//> using dep io.github.kostaskougios::terminal21-ui-std:_VERSION_
```

Change `_VERSION_` with the terminal 21 latest version: ![artifact](https://img.shields.io/maven-central/v/io.github.kostaskougios/terminal21-server_3)

See [project.scala](../example-scripts/project.scala)

## Creating a hello world app

To do this we can create a [hello-world.sc](../example-scripts/hello-world.sc) in our folder.

```scala
#!/usr/bin/env -S scala-cli project.scala
// ------------------------------------------------------------------------------
// Hello world with terminal21.
// Run with ./hello-world.sc
// ------------------------------------------------------------------------------

import org.terminal21.client.*
import org.terminal21.client.components.*
// std components like Paragraph, https://github.com/kostaskougios/terminal21-restapi/blob/main/terminal21-ui-std/src/main/scala/org/terminal21/client/components/StdElement.scala
import org.terminal21.client.components.std.*

Sessions
  .withNewSession("hello-world", "Hello World Example")
  .connect: session =>
    given ConnectedSession = session

    Controller.noModel(Paragraph(text = "Hello World!")).render()
    // since this is a read-only UI, we can exit the app but leave the session open for the user to examine the page.
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
Sessions
  .withNewSession("hello-world", "Hello World Example")
  .connect: session =>
    ...
```

![hello-world](images/hello-world.png)

Next is the actual user interface, in this example just a paragraph with a "Hello World!". In order for it to be rendered, we
quickly construct a Controller (terminal21 uses an MVC architecture idiomatic to scala, more on this later on):

```scala
Controller.noModel(Paragraph(text = "Hello World!")).render()
```

The `render()` method sends the UI components to the server which in turn sends it to the terminal21 react frontend so that it is rendered.

Finally, because this is just a presentation script (we don't expect any feedback from the user), we can terminate it but
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

// ------------------------------------------------------------------------------
// Universe creation progress bar demo
// Run with ./progress.sc
// ------------------------------------------------------------------------------

import org.terminal21.client.{*, given}
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.{ClientEvent, SessionOptions}

Sessions
  .withNewSession("universe-generation", "Universe Generation Progress")
  .connect: session =>
    given ConnectedSession = session

    def components(model: Int, events: Events): MV[Int] =
      val status =
        if model < 10 then "Generating universe ..."
        else if model < 30 then "Creating atoms"
        else if model < 50 then "Big bang!"
        else if model < 80 then "Inflating"
        else "Life evolution"

      val msg = Paragraph(text = status)
      val progress = Progress(value = model)

      MV(
        model + 1,
        Seq(msg, progress)
      )

    // send a ticker to update the progress bar
    object Ticker extends ClientEvent
    fiberExecutor.submit:
      for _ <- 1 to 100 do
        Thread.sleep(200)
        session.fireEvent(Ticker)

    Controller(components)
      .render(1)
      .iterator
      .takeWhile(_.model < 100) // terminate when model == 100
      .foreach(_ => ()) // and run it
    // clear UI
    session.render(Seq(Paragraph(text = "Universe ready!")))
    session.leaveSessionOpenAfterExiting()
```

We start by declaring our components into a function:
```scala
def components(model: Int, events: Events): MV[Int]
```
This kind of function is the standard way to create reusable UI components in terminal21. It takes the model (the progress so far as an Int between 0 and 100),
`Events` which holds any event that was received and returns with a model-view class `MV[Int]` because our model is an `Int`. The top-level component of a page
must have this signature (there are variations but it has to return an `MV`) but sub-components can be any functions with any number of arguments or return type. More on that later.

We then create a paragraph and a progress bar.

```scala
  val msg = Paragraph(text = status)
  val progress = Progress(value = model)
```

Finally, we return the changed model and view:

```scala
  MV(
    model + 1,
    Seq(msg, progress)
  )
```

Ok we got our component, but how does it know when to update the progress and increase the model by 1?
For that we need to send it a custom event. The `components` function is called once when we call the
`render()` method on the controller and once for each event received. Since we don't have any UI component
that may send an event, we will send it ourselfs in a separate fiber:

```scala
// send a ticker to update the progress bar
object Ticker extends ClientEvent
fiberExecutor.submit:
  for _ <- 1 to 100 do
    Thread.sleep(200)
    session.fireEvent(Ticker)
```

Remember the `events: Events` in our `components` function? This will contain the `Ticker` event, but it is of no use, so
the components function ignores it.

Now we can create the `Controller` and iterate through all events:

```scala
Controller(components)
  .render(1) // render takes the initial model value, in this case our model is the progress as an Int between 0 and 100. We start with 1 and increment it in the components function
  .iterator // this is a blocking iterator with events. If there is no event it will block.
  .takeWhile(_.model < 100) // terminate when model == 100
  .foreach(_ => ()) // and run it
```

Thats it. We have a progress bar that displays different messages depending on the stage of our universe creation. And our code would
also be easily testable. The `components` is just a function that returns the model and the UI components, so we can easily assert what
is rendered based on the model value and if the model is updated correctly based on events. A nicer way to structure each page of
our user interface would be to have it in a class with a `components` and a `controller` function. That class would be easily testable,
see the following classes if you would like to find out more. It is an app with 2 pages, a login and loggedin page:

[LoginPage & LoggedInPage](../end-to-end-tests/src/main/scala/tests/LoginPage.scala)

[LoginPageTest](../end-to-end-tests/src/test/scala/tests/LoginPageTest.scala)

[LoggedInTest](../end-to-end-tests/src/test/scala/tests/LoggedInTest.scala)

## Handling clicks

Some UI elements like `Button` are clickable. When the user clicks the element, our controller gets an OnClick event.

Let's see for example [mvc-click-form.sc](../example-scripts/mvc-click-form.sc). We will create a paragraph and a button. When the
user clicks the button, the paragraph text will change and the script will exit.

We will create the Page class we mentioned previously, makes it more structured and easier to test. 

```scala
#!/usr/bin/env -S scala-cli project.scala

// ------------------------------------------------------------------------------
// MVC demo that handles a button click
// Run with ./mvc-click-form.sc
// ------------------------------------------------------------------------------

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.*
import org.terminal21.client.components.chakra.*
import org.terminal21.model.SessionOptions

Sessions
  .withNewSession("mvc-click-form", "MVC form with a button")
  .connect: session =>
    given ConnectedSession = session
    new ClickPage(ClickForm(false)).run() match
      case None        => // the user closed the app
      case Some(model) => println(s"model = $model")

    Thread.sleep(1000) // wait a bit so that the user can see the change in the UI

/** Our model
  *
  * @param clicked
  *   will be set to true when the button is clicked
  */
case class ClickForm(clicked: Boolean)

/** One nice way to structure the code (that simplifies testing too) is to create a class for every page in the user interface. In this instance, we create a
  * page for the click form to be displayed. All components are in `components` method. The controller is in the `controller` method and we can run to get the
  * result in the `run` method. We can use these methods in unit tests to test what is rendered and how events are processed respectively.
  */
class ClickPage(initialForm: ClickForm)(using ConnectedSession):
  def run(): Option[ClickForm] = controller.render(initialForm).run()

  def components(form: ClickForm, events: Events): MV[ClickForm] =
    val button = Button(key = "click-me", text = "Please click me")
    val updatedForm = form.copy(
      clicked = events.isClicked(button)
    )
    val msg = Paragraph(text = if updatedForm.clicked then "Button clicked!" else "Waiting for user to click the button")

    MV(
      updatedForm,
      Seq(msg, button),
      terminate = updatedForm.clicked // terminate the event iteration
    )

  def controller: Controller[ClickForm] = Controller(components)
```

We create the paragraph and button. Components like the `Button` that receive events must have a unique key, so we set that to "click-me":

```scala
val button = Button(key = "click-me", text = "Please click me")
val msg = Paragraph(text = if updatedForm.clicked then "Button clicked!" else "Waiting for user to click the button")
```

If the button is clicked, we update our model accordingly:

```scala
val updatedForm = form.copy(
  clicked = events.isClicked(button)
)
```

Finally we return the `MV` with our model and view. Note that we inform the controller we want to terminate the event iteration when the button is clicked:

```scala
MV(
  updatedForm,
  Seq(msg, button),
  terminate = updatedForm.clicked // terminate the event iteration
)
```

We are good now to run our page:
```scala
  def run(): Option[ClickForm] = controller.render(initialForm).run()
```

The controller renders the form with an initial model of `initialForm`. This effectively just calls our `def components(form: ClickForm, events: Events)` with
an `InitialRender` event and form=initialForm. And then sends the resulting view to the terminal21 server.

Now if we run it with `./on-click.sc` and click the button, the script will terminate with an updated message in the paragraph.

## Reading updated values

Some UI element values, like input boxes, can be changed by the user. We can read the changed value and update our model accordingly.

Lets create a form with an inputbox where the user can enter his/her email and a submit button. Lets follow our Page & Form class approach, it may make
our code a bit longer but also more structured and easier to test.

[mvc-user-form.sc](../example-scripts/mvc-user-form.sc)

```scala
#!/usr/bin/env -S scala-cli project.scala

import org.terminal21.client.*
import org.terminal21.client.components.*
import org.terminal21.client.components.std.Paragraph
import org.terminal21.client.components.chakra.*

// ------------------------------------------------------------------------------
// MVC demo with an email form
// Run with ./mvc-user-form.sc
// ------------------------------------------------------------------------------

Sessions
  .withNewSession("mvc-user-form", "MVC example with a user form")
  .connect: session =>
    given ConnectedSession = session
    new UserPage(UserForm("my@email.com", false)).run match
      case Some(submittedUser) =>
        println(s"Submitted: $submittedUser")
      case None =>
        println("User closed session without submitting the form")

/** Our model for the form */
case class UserForm(
    email: String, // the email
    submitted: Boolean // true if user clicks the submit button, false otherwise
)

/** One nice way to structure the code (that simplifies testing too) is to create a class for every page in the user interface. In this instance, we create a
  * page for the user form to be displayed. All components are in `components` method. The controller is in the `controller` method and we can run to get the
  * result in the `run` method. We can use these methods in unit tests to test what is rendered and how events are processed respectively.
  */
class UserPage(initialForm: UserForm)(using ConnectedSession):

  /** Runs the form and returns the results
    * @return
    *   if None, the user didn't submit the form (i.e. closed the session), if Some(userForm) the user submitted the form.
    */
  def run: Option[UserForm] =
    controller.render(initialForm).run().filter(_.submitted)

  /** @return
    *   all the components that should be rendered for the page
    */
  def components(form: UserForm, events: Events): MV[UserForm] =
    val emailInput = Input(key = "email", `type` = "email", defaultValue = initialForm.email)
    val submitButton = Button(key = "submit", text = "Submit")

    val updatedForm = form.copy(
      email = events.changedValue(emailInput, form.email),
      submitted = events.isClicked(submitButton)
    )

    val output = Paragraph(text = if events.isChangedValue(emailInput) then s"Email changed: ${updatedForm.email}" else "Please modify the email.")

    MV(
      updatedForm,
      Seq(
        QuickFormControl()
          .withLabel("Email address")
          .withInputGroup(
            InputLeftAddon().withChildren(EmailIcon()),
            emailInput
          )
          .withHelperText("We'll never share your email."),
        submitButton,
        output
      ),
      terminate = updatedForm.submitted // terminate the form when the submit button is clicked
    )

  def controller: Controller[UserForm] = Controller(components)
```

The important bit is here:

```scala
val emailInput = Input(key = "email", `type` = "email", defaultValue = initialForm.email)
val submitButton = Button(key = "submit", text = "Submit")

val updatedForm = form.copy(
  email = events.changedValue(emailInput, form.email),
  submitted = events.isClicked(submitButton)
)

val output = Paragraph(text = if events.isChangedValue(emailInput) then s"Email changed: ${updatedForm.email}" else "Please modify the email.")
```

When we update the model, we set `email = events.changedValue(emailInput, form.email)`. If the event was an `OnChange` event for our `emailInput`, this will set the email
to the changed value. If not it will revert back to the `form.email`, effectively leaving the email unchanged.

## Creating reusable UI components.

When we create user interfaces, often we want to reuse our own components. 

For instance we may want a component that asks the name of a `Person`. But we want to also be able
to add this component inside another component that is a table of `Seq[Person]` which lists all people and allows the user
to edit them.

With terminal21, a component is just a function. It would normally take a model and `Events` but not necessarily, i.e. there can
be components that don't have to process events. Also the return
value is up to us, usually we would need to return at least a `UiElement` like `Paragraph` but many times return the updated model too.
The component that renders a page should return `MV[Model]` but the rest of the components can return what they see fit.

Let's see the `Person` example. Here we have 2 components, `personComponent` that asks for the name of a particular `Person` and
`peopleComponent` that renders a table with a Seq[Person], using the `personComponent`. 

```scala
case class Person(id: Int, name: String)
def personComponent(person: Person, events: Events): MV[Person] =
  val nameInput = Input(s"person-${person.id}", defaultValue = person.name)
  val component = Box()
    .withChildren(
      Text(text = "Name"),
      nameInput
    )
  MV(
    person.copy(
      name = events.changedValue(nameInput, person.name)
    ),
    component
  )

def peopleComponent(people: Seq[Person], events: Events): MV[Seq[Person]] =
  val peopleComponents = people.map(p => personComponent(p, events))
  val component        = QuickTable("people")
    .withRows(peopleComponents.map(p => Seq(p.view)))
  MV(peopleComponents.map(_.model), component)
```

`personComponent` take a `Person` model, renders an input box for the person's name and also if there is a change event for this input it updates the model accordingly.
Now `peopleComponent` creates a table and each row contains the `personComponent`. The `Seq[Person]` model is updated accordingly depending on changes propagating from `personComponent`.
