package functions.tastyextractor.model

case class EType(name: String, code: String, typeArgs: Seq[EType], vals: List[EParam], scalaDocs: Option[String], methods: Seq[EMethod]):
  def isUnit: Boolean        = code == "scala.Unit"
  def simplifiedCode: String = if typeArgs.isEmpty then name else s"$name[${typeArgs.map(_.simplifiedCode).mkString(", ")}]"

object EType:
  def code(name: String, code: String) = EType(name, code, Nil, Nil, None, Nil)
