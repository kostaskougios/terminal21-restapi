# Std

These are standard html elements but please prefer the more flexible chakra component if it exists.

[Example](../end-to-end-tests/src/main/scala/tests/StdComponents.scala)

Dependency: `io.github.kostaskougios::terminal21-ui-std:$VERSION`

### Paragraph, NewLine, Span, Em

```scala
Paragraph(text = "Hello World!").withChildren(
    NewLine(),
    Span(text = "Some more text"),
    Em(text = " emphasized!"),
    NewLine(),
    Span(text = "And the last line")
)
```
### Header

```scala
Header1(text = "Welcome to the std components demo/test")
```

### Input

```scala
    val input  = Input(defaultValue = "Please enter your name")
    val output = Paragraph(text = "This will reflect what you type in the input")
    input.onChange: newValue =>
      output.withText(newValue).renderChanges()
```

### Cookies

Set a cookie:

```scala
Cookie(name = "cookie-name", value = "cookie value")
```

Read a cookie:

```scala
val cookieReader = CookieReader(key = "cookie-reader", name = "cookie-name")
val cookieValue   = events.changedValue(cookieReader)
```