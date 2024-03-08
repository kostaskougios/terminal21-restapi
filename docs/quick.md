# Quick classes

There are some UI components, like tables, that require a lot of elements: TableContainer, TBody, Tr, Th etc. `Quick*` classes 
simplify creation of these components.

## QuickTable

This class helps create tables quickly.

```scala
val conversionTable = QuickTable().headers("To convert", "into", "multiply by")
  .caption("Imperial to metric conversion factors")
val tableRows:Seq[Seq[String]] = Seq(
  Seq("inches","millimetres (mm)","25.4"),
  ...
)
conversionTable.rows(tableRows)
```

## QuickTabs

This class simplifies the creation of tabs.

```scala

QuickTabs()
  .withTabs("Tab 1", "Tab 2")
  .withTabPanels(
    Paragraph(text="Tab 1 content"),
    Paragraph(text="Tab 2 content")
  )

```

## QuickFormControl

Simplifies creating forms.

```scala
QuickFormControl()
  .withLabel("Email address")
  .withHelperText("We'll never share your email.")
  .withInputGroup(
    InputLeftAddon().withChildren(EmailIcon()),
    emailInput,
    InputRightAddon().withChildren(CheckCircleIcon())
  )
```