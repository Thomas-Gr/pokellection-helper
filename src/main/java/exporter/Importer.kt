package exporter

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import common.APP_DATA_SOURCE_PATH
import common.SPREADSHEET_ID
import exporter.util.*
import exporter.util.SheetsCredentialProvider.getCredentials
import java.io.File

private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
private const val APPLICATION_NAME = "Pokellection helper"
private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
private const val RANGE = "!A2:N"

val EXPANSIONS_BINDINGS = mapOf(
    "Crossing the Ruins..." to "Crossing the Ruins",
    "Darkness, and to Light..." to "Darkness, and to Light",
    "Gold, Silver, to a New World..." to "Gold, Silver, to a New World",
    "How I Became a Pokémon Card" to "How I Became a Pokemon Card",
    "Magma VS Aqua: Two Ambitions" to "Magma VS Aqua Two Ambitions",
    "Pokémon Jungle" to "Pokemon Jungle",
    "Pokémon Song Best Collection" to "Pokemon Song Best Collection",
    "Pokémon VS" to "Pokemon VS",
    "Pokémon Web" to "Pokemon Web",
    "Pokémon-e Starter Deck" to "Pokemon-e Starter Deck"
)

data class Data(
    val illustrator: String,
    val name: String,
    val frenchName: String,
    val japaneseName: String
)
fun main() {
  Importer().import(SPREADSHEET_ID)
}
class Importer {

  fun import(spreadsheetId: String) {
    val spreadsheets = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build()
        .spreadsheets()

    val allSeries = getAllSeries(File(APP_DATA_SOURCE_PATH))

    val dataFromSheets = getAllSheetTitles(spreadsheets, spreadsheetId)
        .keys
        .filter { it != "Home" }
        .map { it to transform(it, spreadsheets) }
        .toMap()

    for (serie in allSeries) {
      if (dataFromSheets.containsKey(serie.name)) {
        val updatedData = dataFromSheets[serie.name]!!

        for (card in serie.cards.values) {
          if (updatedData.containsKey(card.id)) {
            card.illustrator = updatedData.getValue(card.id).illustrator
            card.name = updatedData.getValue(card.id).name
            card.frenchName = updatedData.getValue(card.id).frenchName
            card.japaneseName = updatedData.getValue(card.id).japaneseName
          }
        }
      }

      val formattedSerie = ObjectMapper().writeValueAsString(serie)

      File("%s/%s.json".format(APP_DATA_SOURCE_PATH, EXPANSIONS_BINDINGS[serie.name] ?: serie.name))
          .writeText(formattedSerie)
    }

    println(dataFromSheets)
  }

  private fun transform(title: String, spreadsheets: Sheets.Spreadsheets): Map<Int, Data> {
    return spreadsheets.values()
        .get(SPREADSHEET_ID, "%s%s".format(title, RANGE))
        .setValueRenderOption("FORMULA")
        .setDateTimeRenderOption("FORMATTED_STRING")
        .execute()
        .getValues()
        .filter { it.size >= 14 }
        .map {it[13].toString().toInt() to Data(
            it[3].toString(),
            it[4].toString(),
            it[5].toString(),
            it[6].toString())}
        .toMap()
  }

}
