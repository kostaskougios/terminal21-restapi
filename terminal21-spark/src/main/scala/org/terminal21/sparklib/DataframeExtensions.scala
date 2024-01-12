package org.terminal21.sparklib

import org.apache.spark.sql.{DataFrame, Row}

extension (rows: Seq[Row])
  def toUiTable: Seq[Seq[String]] = rows.map: row =>
    row.toSeq.map(_.toString)
