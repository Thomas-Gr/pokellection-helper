package exporter

import common.APP_DATA_SOURCE_PATH
import common.SPREADSHEET_ID
import java.io.File

fun main() {
  val exporter = Exporter()
  val homePageExporter = HomePageExporter()

  val allSeries = getAllSeries(File(APP_DATA_SOURCE_PATH))

  homePageExporter.exportToSheet(SPREADSHEET_ID, allSeries)

  allSeries.forEach {
      exporter.exportToSheet(SPREADSHEET_ID, it)
      Thread.sleep(5000L)
  }

}