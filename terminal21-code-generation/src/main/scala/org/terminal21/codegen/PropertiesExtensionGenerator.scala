package org.terminal21.codegen

import functions.tastyextractor.StructureExtractor
import functions.tastyextractor.model.EType
import org.terminal21.codegen.PropertiesExtensionGenerator.extract

import java.io.File
object PropertiesExtensionGenerator:
  private val e = StructureExtractor()

  def extract(tasty: String): Seq[Code] =
    val packages = e.fromFiles(List(tasty))
    packages.map: p =>
      val ext = p.types
        .filterNot(_.name.contains("$"))
        .map: t =>
          createExtension(t)

      Code(
        "-",
        s"""
         |package ${p.name}
         |${ext.mkString("\n")}""".stripMargin
      )

  def createExtension(t: EType): String =
    val methods = t.vals.map: vl =>
      s"def ${vl.name}(v: ${vl.`type`.simplifiedCode}) = e.copy(${vl.name} = v)"

    s"""
       |extension (e: Paragraph)
       |  ${methods.mkString("\n  ")}
       |""".stripMargin

@main def propertiesExtensionGeneratorApp(): Unit =
  val targetDir  = new File("../terminal21-ui-std/target/")
  val scala3Dir  = targetDir.listFiles().find(_.getName.startsWith("scala-3")).get
  val classesDir = new File(scala3Dir, "classes")
  val codes       = extract(classesDir.getAbsolutePath + "/org/terminal21/client/components/std/Paragraph.tasty")
  println(codes.mkString("\n"))
