package functions.tastyextractor.model

case class EPackage(name: String, imports: Seq[EImport], types: Seq[EType]):
  def toPath: String = name.replace('.', '/')
