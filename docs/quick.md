# Quick classes

There are some UI components, like tables, that require a lot of elements: TableContainer, TBody, Tr, Th etc. `Quick*` classes 
simplify creation of this components.

## QuickTable

```scala
val conversionTable = QuickTable().headers("To convert", "into", "multiply by")
  .caption("Imperial to metric conversion factors")
val tableRows:Seq[Seq[String]] = Seq(
  Seq("inches","millimetres (mm)","25.4"),
  ...
)
conversionTable.rows(tableRows)
```
