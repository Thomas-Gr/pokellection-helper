package exporter

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import exporter.util.SheetsCredentialProvider.getCredentials
import exporter.util.addValuesToSheet
import exporter.util.createHeader
import exporter.util.createSheet
import exporter.util.getAllSheetTitles
import types.Serie

private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
private const val APPLICATION_NAME = "Pokellection helper"
private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()

class HomePageExporter {
  fun exportToSheet(spreadsheetId: String, series: List<Serie>) {
    val spreadsheets = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build()
        .spreadsheets()

    val allSheetTitles = getAllSheetTitles(spreadsheets, spreadsheetId)

    val data = series
        .map {
          val escapedName = nameUpdater(it.name).replace("'",  "''")
          listOf(
              "=\"%s\"".format(nameUpdater(it.name)),
              "=COUNTIF('%s'!D2:D%s;\"*, *\")".format(escapedName, it.cards.size + 1),
              "=COUNTBLANK('%s'!F2:F%s)".format(escapedName, it.cards.size + 1),
              "=COUNTBLANK('%s'!G2:G%s)".format(escapedName, it.cards.size + 1),
              "=HYPERLINK(\"#gid=%s\"; \"Go to\")".format(allSheetTitles[nameUpdater(it.name)] ?: "")
          )
        }

    createHeader(spreadsheets, spreadsheetId, "'%s'!A1:F".format("Home"), listOf("Serie","Too many illustrators","Missing French names","Missing Japanese Names", "Link", "Comments"))
    addValuesToSheet(spreadsheets, spreadsheetId, "'%s'!A1:F".format("Home"), data)

    /*
    val createSheet = createSheet("Home")
    spreadsheets.batchUpdate(spreadsheetId, BatchUpdateSpreadsheetRequest()
        .setRequests(listOf(createSheet)))
        .execute()
     */
  }
}
