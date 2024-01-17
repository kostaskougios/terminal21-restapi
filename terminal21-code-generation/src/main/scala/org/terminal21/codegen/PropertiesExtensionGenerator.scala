package org.terminal21.codegen

import functions.tastyextractor.StructureExtractor
import functions.tastyextractor.model.EType
import org.terminal21.codegen.PropertiesExtensionGenerator.extract

import java.io.File
object PropertiesExtensionGenerator:
  private val e = StructureExtractor()

  def extract(tastys: List[String]): Code =
    val packages = e.fromFiles(tastys)
    val ext      = packages.map: p =>
      val extCode = p.types
        .filterNot(_.name.contains("$"))
        .map: t =>
          createExtension(t)

      extCode.mkString("\n")

    val p = packages.head
    Code(
      s"${p.name.replace('.', '/')}/extensions.scala",
      s"""
         |package ${p.name}
         |${ext.mkString("\n")}
         |""".stripMargin
    )

  def createExtension(t: EType): String =
    val methods = t.vals.map: vl =>
      s"def ${vl.name}(v: ${vl.`type`.simplifiedCode}) = e.copy(${vl.name} = v)"

    s"""
       |extension (e: ${t.name})
       |  ${methods.mkString("\n  ")}
       |""".stripMargin

@main def propertiesExtensionGeneratorApp(): Unit =
  val targetDir  = new File("../terminal21-ui-std/target/")
  val scala3Dir  = targetDir.listFiles().find(_.getName.startsWith("scala-3")).get
  val classesDir = new File(scala3Dir, "classes/org/terminal21/client/components/std/")
  val code       = extract(classesDir.listFiles().filter(_.getName.endsWith(".tasty")).filterNot(_.getName.contains("$")).map(_.getAbsolutePath).toList)
  println(code)
