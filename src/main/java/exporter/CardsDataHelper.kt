package exporter

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.ImmutableMap
import types.Card
import types.Serie
import java.io.File

private val objectMapper = ObjectMapper()

fun getAllSeries(folder: File): List<Serie> {
  return folder.walk()
      .filter { !it.isDirectory }
      .filter { !it.name.contains("DS_Store") }
      .map { objectMapper.readValue(it, Serie::class.java) }
      .toList()
}

fun nameUpdater(name: String): String = name.replace(":", ",")

fun mapRawData(card: Card): List<String> {
  return listOf(
      "=image(\"https://github.com/Thomas-Gr/Pokellection/blob/master/resources/images/cards/%s?raw=true\")".format(card.picture),
      if (card.wikiLink == null) {
        ""
      } else {
        "=HYPERLINK(\"https://bulbapedia.bulbagarden.net/wiki/%s\"; \"%s\")".format(card.wikiLink, card.wikiLink)
      },
      escapeNumber(card.number),
      card.illustrator?.toString().orEmpty(),
      card.name?.toString().orEmpty(),
      card.frenchName?.toString().orEmpty(),
      card.japaneseName?.toString().orEmpty(),
      escapeNumber(card.pokemonNumber),
      card.type.name,
      if (card.type2 == null) "" else card.type2.name,
      card.rarity.name,
      card.count.toString(),
      if (card.explanation?.toString().orEmpty().startsWith("'")) {
        "=\"%s\"".format(card.explanation?.toString().orEmpty())
      } else {
        card.explanation?.toString().orEmpty()
      },
      card.id.toString())
}

private fun escapeNumber(value: Int) = if (value == -42) { "/" } else { value.toString() }

fun parseInt(v: String): Int {
  return if (v == "/") -42 else v.toInt()
}

class CardsDataHelper {
  companion object : Comparator<List<String>> {

    val typeOrder = ImmutableMap.builder<String, Int>()
        .put("GRASS", 1)
        .put("FIRE", 2)
        .put("WATER", 3)
        .put("LIGHTNING", 4)
        .put("PSYCHIC", 5)
        .put("FIGHTING", 6)
        .put("COLORLESS", 7)
        .put("METAL", 8)
        .put("DARKNESS", 9)
        .put("TRAINER", 10)
        .put("EXTRA_RULE", 11)
        .put("PASS_CARD", 12)
        .put("ARTWORK", 13)
        .put("DECK_LIST", 14)
        .put("ENERGY", 15)
        .build()

    override fun compare(a: List<String>, b: List<String>): Int {

      if (parseInt(a[2]) < 0 && parseInt(b[2]) > 0) {
        return 1
      } else if (parseInt(a[2]) > 0 && parseInt(b[2]) < 0) {
        return -1
      }

      val cardNumberDiff = parseInt(a[2]) - parseInt(b[2])
      if (cardNumberDiff != 0) return cardNumberDiff

      if (typeOrder[a[8]] == 15 && typeOrder[b[8]] == 10) {
        return 1
      } else if (typeOrder[a[8]] == 10 && typeOrder[b[8]] == 15) {
        return -1
      }

      val typeA = typeOrder[a[8]] ?: 0
      val typeB = typeOrder[b[8]] ?: 0
      val typeDiff = typeA - typeB
      if (typeDiff != 0) return typeDiff

      val pokemonNumberDiff = parseInt(a[7]) - parseInt(b[7])
      if (pokemonNumberDiff != 0) return pokemonNumberDiff

      val nameDiff = a[3].compareTo(b[3])
      if (nameDiff != 0) return nameDiff

      return a[11].compareTo(b[11])
    }
  }
}