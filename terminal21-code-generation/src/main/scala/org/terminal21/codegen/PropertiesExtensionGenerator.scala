package org.terminal21.codegen

import functions.tastyextractor.StructureExtractor
import org.terminal21.codegen.PropertiesExtensionGenerator.extract

import java.io.File
object PropertiesExtensionGenerator:
  private val e = StructureExtractor()

  def extract(tasty: String) =
    val p     = e.fromFiles(List(tasty))
    val types = p.flatMap(_.types).filterNot(_.name.contains("$"))
    types

@main def propertiesExtensionGeneratorApp(): Unit =
  val targetDir  = new File("../terminal21-ui-std/target/")
  val scala3Dir  = targetDir.listFiles().find(_.getName.startsWith("scala-3")).get
  val classesDir = new File(scala3Dir, "classes")
  val types      = extract(classesDir.getAbsolutePath + "/org/terminal21/client/components/std/Paragraph.tasty")
  for t <- types do
    println(t.name)
    println(t.vals.mkString("\n"))
