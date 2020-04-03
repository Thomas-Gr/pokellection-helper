package exporter

import java.io.File

private val spreadsheetId = "xxxxx"

fun main() {
  val exporter = Exporter()

  val allSeries = getAllSeries(File("xxxx"))

  allSeries.forEach {
      exporter.exportToSheet(spreadsheetId, it)
      Thread.sleep(5000L)
  }

}