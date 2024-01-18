package org.terminal21.codegen

import functions.tastyextractor.StructureExtractor
import functions.tastyextractor.model.EType
import org.terminal21.codegen.PropertiesExtensionGenerator.{extract, generate}

import java.io.File
object PropertiesExtensionGenerator:
  private val e = StructureExtractor()

  def extract(tastys: List[String]): Code =
    val mainCp   = detectClasspath(new File("../terminal21-ui-std"))
    val packages = e.fromFiles(tastys, List(mainCp.getAbsolutePath))
    val ext      = packages.map: p =>
      val extCode = p.types
        .filterNot(_.name.contains("$"))
        .filterNot(_.vals.isEmpty)
        .map: t =>
          createExtension(t)

      extCode.mkString("\n")

    val p = packages.head
    Code(
      s"${p.name.replace('.', '/')}/extensions.scala",
      s"""
         |package ${p.name}
         |
         |// GENERATED WITH PropertiesExtensionGenerator, DON'T EDIT
         |
         |${p.imports.map(_.fullName).mkString("import ", "\nimport ", "")}
         |${ext.mkString("\n")}
         |""".stripMargin
    )

  def fix(n: String) = n match
    case "type" => "`type`"
    case _      => n

  def createExtension(t: EType): String =
    val skipVals = Set("children", "rendered", "style")
    val methods  = t.vals
      .filterNot(v => skipVals(v.name))
      .map: vl =>
        s"def with${vl.name.capitalize}(v: ${vl.`type`.simplifiedCode}) = copy(${fix(vl.name)} = v)"

    s"""
       |extension (e: ${t.name})
       |  ${methods.mkString("\n  ")}
       |""".stripMargin

  def detectClasspath(moduleDir: File) =
    val targetDir  = new File(moduleDir, "target")
    val scala3Dir  = targetDir.listFiles().find(_.getName.startsWith("scala-3")).get
    val classesDir = new File(scala3Dir, "classes")
    classesDir

  def generate(moduleDir: File, pckg: String): Unit =
    println(s"Generating for $pckg")
    val classesRootDir = detectClasspath(moduleDir)
    val classesDir     = new File(classesRootDir, pckg.replace('.', '/'))
    val code           = extract(classesDir.listFiles().filter(_.getName.endsWith(".tasty")).filterNot(_.getName.contains("$")).map(_.getAbsolutePath).toList)
    code.writeTo(s"${moduleDir.getAbsolutePath}/src/main/ui-generated")

@main def propertiesExtensionGeneratorApp(): Unit =
  generate(new File("../terminal21-ui-std"), "org.terminal21.client.components.std")
  generate(new File("../terminal21-ui-std"), "org.terminal21.client.components.chakra")
//  generate(new File("../terminal21-nivo"), "org.terminal21.client.components.nivo")
