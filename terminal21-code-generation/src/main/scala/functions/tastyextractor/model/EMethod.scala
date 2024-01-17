package functions.tastyextractor.model

case class EMethod(name: String, paramss: List[List[EParam]], returnType: EType, scalaDocs: Option[String])
