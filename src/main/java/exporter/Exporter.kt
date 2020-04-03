package exporter

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.Request
import exporter.util.*
import exporter.util.SheetsCredentialProvider.getCredentials
import types.Serie

private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
private const val APPLICATION_NAME = "Pokellection helper"
private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
private val HEADER = listOf("Picture", "Wiki", "Card number", "Name", "French name", "Japanese name", "Pokedex number", "Type", "Rarity", "Card count in set", "Additional details", "id")

class Exporter {

  fun exportToSheet(spreadsheetId: String, serie: Serie) {
    val name = nameUpdater(serie.name)
    val spreadsheets = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build()
        .spreadsheets()

    if (!getAllSheetTitles(spreadsheets, spreadsheetId).containsKey(name)) {
      spreadsheets.batchUpdate(spreadsheetId, BatchUpdateSpreadsheetRequest()
          .setRequests(listOf(createSheet(name))))
          .execute()
    }

    val sheetId = getAllSheetTitles(spreadsheets, spreadsheetId)[name] ?: return

    val data = serie.cards.values.map { mapRawData(it) }.sortedWith(CardsDataHelper).toList()
    val numberOfConditionalFormatting = getNumberOfConditionalFormatting(spreadsheets, spreadsheetId, name)
    val alternateColorRangeId = getAlternateColorRangeId(spreadsheets, spreadsheetId, name)

    val requests = mutableListOf<Request>()
    requests.add(clearSheet(sheetId));
    requests.addAll(removeAllConditionalFormatting(sheetId, numberOfConditionalFormatting))
    if (alternateColorRangeId != 0) {
      requests.add(deleteAlternateColors(alternateColorRangeId))
    }
    requests.addAll(conditionFormatting(sheetId, data))
    requests.add(resizeCells(sheetId, 1))
    requests.add(setAlternateColors(sheetId, data.size))
    requests.add(setBoldHeader(sheetId))

    spreadsheets.batchUpdate(spreadsheetId, BatchUpdateSpreadsheetRequest().setRequests(requests)).execute()

    createHeader(spreadsheets, spreadsheetId, "'%s'!A1:L".format(name), HEADER)
    addValuesToSheet(spreadsheets, spreadsheetId, "'%s'!A2:L".format(name), data)
  }

}
